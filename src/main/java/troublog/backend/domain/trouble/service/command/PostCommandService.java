package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.dto.request.PostUpdateReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostQueryService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandService {

	private final PostRepository postRepository;
	private final PostQueryService postQueryService;
	private final PostFactory postFactory;

	public PostResDto createPost(Long userId, PostCreateReqDto reqDto) {
		Post newPost = postFactory.createPostWithRequireRelations(reqDto, userId);
		Post savedPost = postRepository.save(newPost);
		postFactory.establishSecondaryRelations(savedPost, reqDto);
		return PostConverter.toResponse(savedPost);
	}

	public PostResDto updatePost(Long userId, Long postId, PostUpdateReqDto reqDto) {
		Post foundPost = postQueryService.findPostById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postFactory.updateRelationsIfChanged(reqDto, foundPost);
		Post savedPost = postRepository.save(foundPost);
		return PostConverter.toResponse(savedPost);
	}

	public void deletePost(Long userId, Long postId) {
		Post foundPost = postQueryService.findPostById(postId);
		PostFactory.validateAuthorized(userId, foundPost);
		postRepository.delete(foundPost);
	}
}