package troublog.backend.domain.trouble.service.facade.command.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.ai.summary.converter.SummaryTaskConverter;
import troublog.backend.domain.ai.summary.dto.response.TaskStartResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.service.PostSummaryService;
import troublog.backend.domain.ai.summary.service.facade.SummaryTaskFacade;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.domain.trouble.service.command.PostCommandService;
import troublog.backend.domain.trouble.service.facade.command.PostCommandFacade;
import troublog.backend.domain.trouble.service.facade.relation.impl.PostRelationFacadeImpl;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.usecase.PostCreateUseCase;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandFacadeImpl implements PostCommandFacade {

	private final PostCreateUseCase postCreateUseCase;
	private final PostCommandService postCommandService;
	private final PostQueryService postQueryService;
	private final PostRelationFacadeImpl postRelationFacade;
	private final PostSummaryService postSummaryService;
	private final SummaryTaskFacade summaryTaskFacade;

	public PostResDto createPost(final Long userId,final PostReqDto postReqDto) {
		return postCreateUseCase.createPost(userId, postReqDto);
	}

	public PostResDto updatePost(Long userId, Long postId, PostReqDto reqDto) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postRelationFacade.updateRelationsIfChanged(reqDto, foundPost);
		return PostConverter.toResponse(foundPost);
	}

	public void softDeletePost(Long userId, Long postId) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		foundPost.markAsDeleted();
	}

	public void hardDeletePost(Long userId, Long postId) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postCommandService.deletePost(foundPost);
	}

	public PostResDto restorePost(Long userId, Long postId) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		PostFactory.validateIsDeleted(foundPost);
		foundPost.restoreFromDeleted();
		return PostConverter.toResponse(foundPost);
	}

	public TaskStartResDto startSummary(Long userId, SummaryType summaryType, Long postId) {
		SummaryTask task = summaryTaskFacade.createTask(postId, userId, summaryType);
		postSummaryService.executeAsync(task, summaryType);
		return SummaryTaskConverter.toStartResponseDto(task, userId);
	}
}