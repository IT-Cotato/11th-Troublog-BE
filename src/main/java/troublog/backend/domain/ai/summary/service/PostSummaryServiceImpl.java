package troublog.backend.domain.ai.summary.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.dto.ExtractedContentDto;
import troublog.backend.domain.ai.summary.dto.SummarizedResDto;
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.service.facade.PostQueryFacade;
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
	private final PostTagQueryService tagQueryService;
	private final PostQueryFacade postQueryFacade;

	@Override
	public SummarizedResDto execute(String id, String type) {
		validateInput(id);
		return performAiAnalysis(id, type);
	}

	@Override
	public CompletableFuture<SummarizedResDto> executeAsync(String id, String type) {
		return CompletableFuture.supplyAsync(() -> execute(id, type));
	}

	@Override
	public boolean validateInput(String input) {
		return true;
	}

	private SummarizedResDto performAiAnalysis(String id, String type) {
		BeanOutputConverter<SummarizedResDto> converter = new BeanOutputConverter<>(SummarizedResDto.class);
		Post post = postQueryFacade.findPostById(id);
		PromptTemplate promptTemplate = generatePromptTemplate(post, type);

		String aiResponse = callAiService(promptTemplate, converter);
		return convertAiResponse(aiResponse, converter);
	}

	private String callAiService(PromptTemplate promptTemplate, BeanOutputConverter<SummarizedResDto> converter) {
		String response = chatClient.prompt()
			.system(promptProperties.system())
			.user(promptTemplate.render() + converter.getFormat())
			.call()
			.content();

		if (response == null || response.trim().isEmpty()) {
			log.error("[AI] AI 응답이 비어있습니다.");
			throw new RuntimeException("AI 처리 실패");
		}

		return response;
	}

	private SummarizedResDto convertAiResponse(String aiResponse,
		BeanOutputConverter<SummarizedResDto> converter) {
		try {
			return converter.convert(aiResponse);
		} catch (Exception e) {
			log.error("[AI] 응답 변환 실패: {}", e.getMessage());
			throw new RuntimeException("AI 변환 실패", e);
		}
	}

	private PromptTemplate generatePromptTemplate(Post post, String type) {
		try {
			ContentSummaryType summaryType = ContentSummaryType.from(type);
			PromptTemplate promptTemplate = new PromptTemplate(selectPrompt(summaryType));
			promptTemplate.add("title", post.getTitle());
			promptTemplate.add("errorTag", tagQueryService.findAllByPostId(post.getId()));
			promptTemplate.add("postTag", post.getTitle());
			promptTemplate.add("content", extractFromContent(post.getContents()));
			return promptTemplate;
		} catch (Exception e) {
			log.error("[AI] 프롬프트 생성 실패: {}", e.getMessage());
			throw new RuntimeException("AI 프롬프트 생성 실패", e);
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
}
