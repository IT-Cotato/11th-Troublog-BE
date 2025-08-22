package troublog.backend.domain.trouble.service.query;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.converter.ListConverter;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.repository.PostSummaryRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryService {

	private final PostRepository postRepository;
	private final PostSummaryRepository postSummaryRepository;

	public Post findById(Long id) {
		log.info("[Post] 트러블슈팅 조회:: postId={}", id);
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

	public List<Post> findPostByStatusAndUserId(Long userId, PostStatus status) {
		List<Post> posts = (status == null)
			? postRepository.findByUserIdAndIsDeletedFalse(userId)
			: postRepository.findByUserIdAndStatusAndIsDeletedFalse(userId, status);
		log.info("[Post] 사용자 {} 상태 트러블슈팅 문서 개수 조회: userId={}, count={}",
			(status == null ? "ALL" : status), userId, posts.size());
		return posts;
	}

	@Transactional(readOnly = true)
	public Page<Post> getAllTroubles(Long userId, Pageable pageable) {
		Page<Post> page = postRepository.findAllByUser_IdAndIsDeletedFalse(userId, pageable);
		log.info("[Post] 전체 트러블 조회: userId={}, total={}, page={}, size={}, elementsInPage={}",
			userId, page.getTotalElements(), page.getNumber(), page.getSize(), page.getNumberOfElements());
		return page;
	}

	@Transactional(readOnly = true)
	public Page<Post> getAllTroublesOrderByStarRating(Long userId, Pageable pageable) {
		Page<Post> page = postRepository.findTroublesByUserOrderByStarRating(userId, pageable);
		log.info("[Post] 전체 트러블 조회 (중요도순): userId={}, total={}, page={}, size={}, elementsInPage={}",
			userId, page.getTotalElements(), page.getNumber(), page.getSize(), page.getNumberOfElements());
		return page;
	}

	public List<TroubleListResDto> getWritingAndCompletedTroubles(
		Long projectId, SortType sort, VisibilityType visibility, PostStatus status) {
		Boolean visible = mapVisibility(visibility);
		List<Post> posts = (sort == SortType.IMPORTANT)
			? postRepository.findByProjectImportant(projectId, status, visible)
			: postRepository.findByProject(projectId, status, visible);
		if (posts.isEmpty()) {
			return List.of();
		}

		if (status == PostStatus.WRITING) {
			log.info("[Post] 작성중인 트러블슈팅 문서 조회: projectId={}, postCount={}", projectId, posts.size());
		} else if (status == PostStatus.COMPLETED) {
			log.info("[Post] 작성완료된 트러블슈팅 문서 조회: projectId={}, postCount={}", projectId, posts.size());
		}

		return posts.stream()
			.map(ListConverter::toAllTroubleListResDto)
			.toList();
	}

	public List<TroubleListResDto> getSummarizedTroubles(
		Long projectId, SortType sort, SummaryType summaryType
	) {
		SummaryType st = (summaryType == SummaryType.NONE) ? null : summaryType;
		List<PostSummary> posts = (sort == SortType.IMPORTANT)
			? postSummaryRepository.findByProjectSummarizedImportant(projectId, PostStatus.SUMMARIZED, st)
			: postSummaryRepository.findByProjectSummarized(projectId, PostStatus.SUMMARIZED, st,
			Sort.by(DESC, "created_at", "id"));

		if (posts.isEmpty())
			return List.of();

		log.info("[Post] 요약완료된 트러블슈팅 문서 조회: postCount={}", posts.size());
		return posts.stream()
			.map(ListConverter::toAllSummerizedListResDto)
			.toList();
	}

	private Boolean mapVisibility(VisibilityType v) {
		if (v == null || v == VisibilityType.ALL)
			return null;
		return (v == VisibilityType.PUBLIC) ? Boolean.TRUE : Boolean.FALSE;
	}

	public Page<Post> getCommunityPosts(Pageable pageable) {
		return postRepository.getCommunityPosts(pageable);
	}

	public List<Post> findByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			log.info("[Post] 최근 열람 DB 조회: requested=0, found=0");
			return List.of();
		}
		List<Post> posts = postRepository.findByIdIn(ids);
		log.info("[Post] 최근 열람 DB 조회: requested={}, found={}", ids.size(), posts.size());
		return posts;
	}
}