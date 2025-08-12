package troublog.backend.domain.trouble.service.query;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.VisibilityType;
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

	public Page<Post> getAllTroubles(Long userId, Pageable pageable) {
		// 최신순 기준 선택 필요 - createdAt/updatedAt/completedAt
		Sort sort = Sort.by(DESC, "completedAt", "id");

		Pageable pageReq = PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			sort
		);

		Page<Post> page = postRepository.findAllByUser_IdAndIsDeletedFalse(userId, pageReq);
		log.info("[Post] 전체 트러블 조회: userId={}, total={}, page={}, size={}, elementsInPage={}",
			userId, page.getTotalElements(), page.getNumber(), page.getSize(), page.getNumberOfElements());
		return page;
	}

	public List<TroubleListResDto> getCompletedTroubles(
		Long projectId, SortType sort, VisibilityType visibility) {
		Boolean visible = mapVisibility(visibility);
		List<Post> posts = (sort == SortType.IMPORTANT)
			? postRepository.findByProjectCompletedImportant(projectId, PostStatus.COMPLETED, visible)
			: postRepository.findByProjectCompleted(projectId, PostStatus.COMPLETED, visible,
			Sort.by(DESC, "completedAt", "id"));
		if (posts.isEmpty())
			return List.of();

		log.info("[Post] 작성완료된 트러블슈팅 문서 조회: postCount={}", posts.size());
		return posts.stream()
			.map(ListConverter::toAllTroubleListResDto)
			.toList();
	}

	public List<TroubleListResDto> getSummarizedTroubles(
		Long projectId, SortType sort, ContentSummaryType summaryType) {
		ContentSummaryType st = (summaryType == ContentSummaryType.NONE) ? null : summaryType;
		List<Post> posts = (sort == SortType.IMPORTANT)
			? postRepository.findByProjectSummarizedImportant(projectId, PostStatus.SUMMARIZED, st)
			: postRepository.findByProjectSummarized(projectId, PostStatus.SUMMARIZED, st,
			Sort.by(DESC, "completedAt", "id"));

		if (posts.isEmpty())
			return List.of();

		log.info("[Post] 요약완료된 트러블슈팅 문서 조회: postCount={}", posts.size());
		return posts.stream()
			.map(ListConverter::toAllTroubleListResDto)
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
}