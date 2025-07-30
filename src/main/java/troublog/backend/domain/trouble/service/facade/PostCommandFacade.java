package troublog.backend.domain.trouble.service.facade;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.dto.request.PostUpdateReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.command.PostCommandService;
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

	public PostResDto createPost(Long userId, PostCreateReqDto reqDto) {
		Post newPost = postFactory.createPostWithRequireRelations(reqDto);
		postRelationFacade.establishRequireRelations(newPost, userId, reqDto);
		Post savedPost = postCommandService.save(newPost);
		postRelationFacade.establishSecondaryRelations(savedPost, reqDto);
		return PostConverter.toResponse(savedPost);
	}

	public PostResDto updatePost(Long userId, Long postId, PostUpdateReqDto reqDto) {
		Post foundPost = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postRelationFacade.updateRelationsIfChanged(reqDto, foundPost);
		Post savedPost = postCommandService.save(foundPost);
		return PostConverter.toResponse(savedPost);
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
}