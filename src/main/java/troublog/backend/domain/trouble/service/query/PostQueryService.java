package troublog.backend.domain.trouble.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryService {
	private final PostRepository postRepository;

	public Post findPostById(Long id) {
		return postRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public PostResDto findPostDetailsById(Long userId, Long id) {
		//TODO N+1 Query 해결 필요
		Post post = findPostById(id);
		PostFactory.validateAuthorized(userId, post);
		return PostConverter.toResponse(post);
	}

	public Post findNotDeletedPost(Long id) {
		return postRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public List<PostResDto> findAllNotDeletedPosts() {
		List<Post> posts = postRepository.findByIsDeletedFalse();
		return PostConverter.toResponseList(posts);
	}

	public List<PostResDto> findDeletedPosts() {
		List<Post> posts = postRepository.findByIsDeletedTrue();
		return PostConverter.toResponseList(posts);

	}
}