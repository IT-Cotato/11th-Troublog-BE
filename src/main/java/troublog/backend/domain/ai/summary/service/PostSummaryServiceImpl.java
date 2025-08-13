package troublog.backend.domain.ai.summary.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.dto.common.ExtractedContentDto;
import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;
import troublog.backend.domain.ai.summary.service.facade.SummaryTaskFacade;
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;
import troublog.backend.domain.trouble.service.query.ContentQueryService;
import troublog.backend.domain.trouble.service.query.PostTagQueryService;
import troublog.backend.global.common.config.PromptProperties;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AiTaskException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostSummaryServiceImpl implements PostSummaryService {

	private final ChatClient chatClient;
	private final PromptProperties promptProperties;
	private final PostTagQueryService postTagQueryService;
	private final PostQueryFacade postQueryFacade;
	private final ContentQueryService contentQueryService;
	private final SummaryTaskFacade summaryTaskFacade;
	private final PostSummaryCompletionService completionService;

	@Override
	public SummarizedResDto execute(SummaryTask summaryTask, ContentSummaryType summaryType) {
		validateInput(summaryTask);
		return performAiAnalysis(summaryTask, summaryType);
	}

	@Override
	@Async("summaryExecutor")
	public CompletableFuture<SummarizedResDto> executeAsync(SummaryTask summaryTask, ContentSummaryType summaryType) {
		return CompletableFuture
			.supplyAsync(() -> executeAiAnalysis(summaryTask, summaryType))
			.thenApply(result -> processResult(summaryTask, result))
			.thenApply(result -> completeTask(summaryTask, result))
			.exceptionally(throwable -> handleFailure(summaryTask, throwable));
	}

	@Override
	public boolean validateInput(SummaryTask summaryTask) {
		return summaryTask != null && summaryTask.getId() != null && summaryTask.getPostId() != null;
	}

	private SummarizedResDto performAiAnalysis(SummaryTask summaryTask, ContentSummaryType summaryType) {
		summaryTaskFacade.updateTask(summaryTask, SummaryStatus.PREPROCESSING);
		BeanOutputConverter<SummarizedResDto> converter = new BeanOutputConverter<>(SummarizedResDto.class);
		Post post = postQueryFacade.findPostById(summaryTask.getPostId());
		PromptTemplate promptTemplate = generatePromptTemplate(post, summaryType);
		summaryTaskFacade.updateTask(summaryTask, SummaryStatus.ANALYZING);
		String aiResponse = callAiService(promptTemplate, converter);
		return convertAiResponse(aiResponse, converter);
	}

	private String callAiService(
		PromptTemplate promptTemplate,
		BeanOutputConverter<SummarizedResDto> converter
	) {
		String response = chatClient.prompt()
			.system(promptProperties.system())
			.user(promptTemplate.render() + converter.getFormat())
			.call()
			.content();

		if (response == null || response.trim().isEmpty()) {
			log.error("[AI] AI 응답이 비어있습니다.");
			throw new AiTaskException(ErrorCode.BAD_REQUEST);
		}

		return response;
	}

	private SummarizedResDto convertAiResponse(String aiResponse,
		BeanOutputConverter<SummarizedResDto> converter) {
		try {
			return converter.convert(aiResponse);
		} catch (Exception e) {
			log.error("[AI] 응답 변환 실패: {}", e.getMessage());
			throw new AiTaskException(ErrorCode.BAD_REQUEST);
		}
	}

	private PromptTemplate generatePromptTemplate(Post post, ContentSummaryType summaryType) {
		try {
			PromptTemplate promptTemplate = new PromptTemplate(selectPrompt(summaryType));
			List<Content> contents = contentQueryService.findAllContentsByPostId(post.getId());
			promptTemplate.add("title", post.getTitle());
			promptTemplate.add("errorTag", postTagQueryService.findTagNamesByPostId(post.getId())); // 올바른 서비스 사용
			promptTemplate.add("postTag", post.getTitle());
			promptTemplate.add("content", extractFromContent(contents));
			return promptTemplate;
		} catch (Exception e) {
			log.error("[AI] 프롬프트 생성 실패: {}", e.getMessage());
			throw new AiTaskException(ErrorCode.BAD_REQUEST);
		}
	}

	private Resource selectPrompt(ContentSummaryType contentSummaryType) {
		return switch (contentSummaryType) {
			case BLOG -> promptProperties.blog();
			case RESUME -> promptProperties.resume();
			case INTERVIEW -> promptProperties.interview();
			case ISSUE_MANAGEMENT -> promptProperties.issueManagement();
			case NONE -> throw new AiTaskException(ErrorCode.INVALID_INPUT);
		};
	}

	private List<ExtractedContentDto> extractFromContent(List<Content> contents) {
		return contents.stream()
			.map(ContentConverter::extractContent)
			.toList();
	}

	private SummarizedResDto executeAiAnalysis(SummaryTask summaryTask, ContentSummaryType summaryType) {
		log.info("AI 분석 작업 시작: taskId={}, postId={}", summaryTask.getId(), summaryTask.getPostId());
		return execute(summaryTask, summaryType);
	}

	private SummarizedResDto processResult(SummaryTask summaryTask, SummarizedResDto result) {
		summaryTask.registerResult(result);
		log.info("AI 분석 결과 등록 완료: taskId={}, postId={}", summaryTask.getId(), summaryTask.getPostId());
		return result;
	}

	private SummarizedResDto completeTask(SummaryTask summaryTask, SummarizedResDto result) {
		return completionService.completeTask(summaryTask, result);
	}

	private SummarizedResDto handleFailure(SummaryTask summaryTask, Throwable throwable) {
		log.error("AI 분석 작업 실패: taskId={}, postId={}, error={}",
			summaryTask.getId(), summaryTask.getPostId(), throwable.getMessage(), throwable);
		summaryTaskFacade.updateTask(summaryTask, SummaryStatus.FAILED);
		throw new AiTaskException(ErrorCode.TASK_UPDATE_FAILED);
	}
}