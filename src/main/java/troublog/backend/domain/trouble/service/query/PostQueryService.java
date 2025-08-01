package troublog.backend.domain.trouble.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryService {
	public static final int DEFAULT_OFFSET = 0;
	public static final int DEFAULT_LIMIT = 100;

	private final PostRepository postRepository;

	public Post findById(Long id) {
		log.info("[Post] 트러블슈팅 문서 조회: postId={}", id);
		return postRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public Post findNotDeletedPost(Long id) {
		log.info("[Post] 삭제되지 않은 트러블슈팅 문서 조회: postId={}", id);
		return postRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public List<Post> findAllNotDeletedPosts() {
		List<Post> posts = postRepository.findByIsDeletedFalse();
		log.info("[Post] 삭제되지 않은 게시글 조회: postCount={}", posts.size());
		return posts;
	}

	public List<Post> findAllDeletedPosts() {
		List<Post> posts = postRepository.findByIsDeletedTrue();
		log.info("[Post] 삭제된 게시글 조회: postCount={}", posts.size());
		return posts;
	}

	public List<Post> searchUserPostByKeyword(Long userId, String keyword) {
		List<Post> posts = postRepository.searchPostsByKeyword(userId, keyword, DEFAULT_OFFSET, DEFAULT_LIMIT);
		log.info("[Post] 검색어 기반 게시글 조회 : keyword={}, postCount={}", keyword, posts.size());
		return posts;
	}

}