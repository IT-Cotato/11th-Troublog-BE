package troublog.backend.domain.trouble.service.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryService {

	private final PostRepository postRepository;

	public Post findById(Long id) {
		log.info("[Post] 트러블슈팅 문서 조회: postId={}", id);
		return postRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public Post findSummaryById(Long id, ContentSummaryType summaryType) {
		log.info("[Post] AI 요약본 조회: postId={}", id);
		return postRepository.findSummaryById(id, summaryType)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public Post findPostWithoutSummaryById(Long id) {
		log.info("[Post] 트러블슈팅 문서 + AI 요약본 조회: postId={}", id);
		return postRepository.findPostWithOutSummaryById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}


	public Post findNotDeletedPost(Long id) {
		log.info("[Post] 삭제되지 않은 트러블슈팅 문서 조회: postId={}", id);
		return postRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public List<Post> findAllNotDeletedPosts() {
		List<Post> posts = postRepository.findByIsDeletedFalse();
		log.info("[Post] 삭제되지 않은 트러블슈팅 문서 조회: postCount={}", posts.size());
		return posts;
	}

	public List<Post> findAllDeletedPosts() {
		List<Post> posts = postRepository.findByIsDeletedTrue();
		log.info("[Post] 삭제된 트러블슈팅 문서 조회: postCount={}", posts.size());
		return posts;
	}

	public Page<Post> searchUserPostByKeyword(Long userId, String keyword, Pageable pageable) {
		Page<Post> postPage = postRepository.searchUserPostsByKeyword(userId, keyword, pageable);
		log.info("[Post] 검색어 기반 사용자 트러블슈팅 문서 조회 : userId={}, keyword={}, postCount={}", userId, keyword,
			postPage.getNumberOfElements());
		return postPage;
	}

	public Page<Post> searchPostByKeyword(String keyword, Pageable pageable) {
		Page<Post> postPage = postRepository.searchPostsByKeyword(keyword, pageable);
		log.info("[Post] 검색어 기반 트러블슈팅 문서 조회 : keyword={}, postCount={}", keyword, postPage.getNumberOfElements());
		return postPage;
	}
	
	public List<Post> findWritingPostsByUserId(Long userId) {
		List<Post> writingPosts = postRepository.findByUserIdAndStatusAndIsDeletedFalse(userId, PostStatus.WRITING);
		log.info("[Post] 사용자 WRITING 상태 트러블슈팅 문서 개수 조회: userId={}, count={}", userId, writingPosts.size());
		return writingPosts;
	}

}