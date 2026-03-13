package troublog.backend.domain.trouble.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.ai.summary.converter.SummaryTaskConverter;
import troublog.backend.domain.ai.summary.dto.response.TaskStartResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.service.PostSummaryService;
import troublog.backend.domain.ai.summary.service.facade.SummaryTaskFacadeService;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.domain.trouble.service.command.PostCommandService;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostQueryService;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandFacadeService {

	private final PostFactory postFactory;
	private final PostCommandService postCommandService;
	private final PostQueryService postQueryService;
	private final PostRelationFacadeService postRelationFacadeService;
	private final PostSummaryService postSummaryService;
	private final SummaryTaskFacadeService summaryTaskFacadeService;

	@Transactional
	public PostResDto createPost(final Long userId, final PostReqDto postReqDto) {
		Post newPost = postFactory.createPostWithRequireRelations(postReqDto);
		postRelationFacadeService.establishRequireRelations(newPost, userId, postReqDto);
		Post savedPost = postCommandService.savePost(newPost);
		postRelationFacadeService.establishSecondaryRelations(savedPost, postReqDto);
		return toPostResponse(savedPost);
	}

	@Transactional
	public PostResDto updatePost(final Long userId, final Long postId, final PostReqDto reqDto) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postRelationFacadeService.updateRelationsIfChanged(reqDto, foundPost);
		return toPostResponse(foundPost);
	}

	@Transactional
	public void softDeletePost(final Long userId, final Long postId) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postCommandService.softDelete(foundPost);
	}

	@Transactional
	public void hardDeletePost(final Long userId, final Long postId) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postCommandService.deletePost(foundPost);
	}

	@Transactional
	public PostResDto restorePost(final Long userId, final Long postId) {
		Post foundPost = postQueryService.findDeletedPostById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postCommandService.restore(foundPost);
		return findRestoredPost(postId);
	}

	private PostResDto findRestoredPost(final Long postId) {
		Post restoredPost = postQueryService.findById(postId);
		return toPostResponse(restoredPost);
	}

	public TaskStartResDto startSummary(final Long userId, final SummaryType summaryType, final Long postId) {
		SummaryTask task = summaryTaskFacadeService.createTask(postId, userId, summaryType);
		postSummaryService.executeAsync(task, summaryType);
		return SummaryTaskConverter.toStartResponseDto(task, userId);
	}

	private PostResDto toPostResponse(final Post post) {
		return PostConverter.toResponse(
			post,
			postQueryService.findErrorTag(post),
			postQueryService.findTechStackTags(post)
		);
	}
}
