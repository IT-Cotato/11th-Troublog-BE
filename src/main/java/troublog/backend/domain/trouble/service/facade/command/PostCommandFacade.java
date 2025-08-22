package troublog.backend.domain.trouble.service.facade.command;

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
import troublog.backend.domain.trouble.service.facade.relation.PostRelationFacade;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostQueryService;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandFacade {

	private final PostFactory postFactory;
	private final PostCommandService postCommandService;
	private final PostQueryService postQueryService;
	private final PostRelationFacade postRelationFacade;
	private final PostSummaryService postSummaryService;
	private final SummaryTaskFacade summaryTaskFacade;

	public PostResDto createPost(Long userId, PostReqDto postReqDto) {
		Post newPost = postFactory.createPostWithRequireRelations(postReqDto);
		postRelationFacade.establishRequireRelations(newPost, userId, postReqDto);
		Post savedPost = postCommandService.save(newPost);
		postRelationFacade.establishSecondaryRelations(savedPost, postReqDto);
		return PostConverter.toResponse(savedPost);
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
		postCommandService.delete(foundPost);
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