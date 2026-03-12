package troublog.backend.domain.trouble.service.query;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.PostViewFilter;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.repository.PostSummaryRepository;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryService {

	private final PostRepository postRepository;
	private final PostSummaryRepository postSummaryRepository;

	public Post findById(long id) {
		log.info("[Post] 트러블슈팅 조회:: postId={}", id);
		return postRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public Post findNotDeletedPost(Long id) {
		log.info("[Post] 삭제되지 않은 트러블슈팅 문서 조회: postId={}", id);
		return postRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}

	public List<Post> findAllNotDeletedPosts() {
		List<Post> posts = postRepository.findAll();
		log.info("[Post] 삭제되지 않은 트러블슈팅 문서 조회: postCount={}", posts.size());
		return posts;
	}

	public List<Post> findAllNotDeletedPostsByUser(User user) {
		List<Post> postList = postRepository.findAllByUser(user);
		log.info("[Post] 특정 유저의 삭제되지 않은 트러블슈팅 문서 조회");
		return postList;
	}

	public List<Post> findAllDeletedPosts() {
		List<Post> posts = postRepository.findAllDeletedPosts();
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
			? postRepository.findByUserId(userId)
			: postRepository.findByUserIdAndStatus(userId, status);
		log.info("[Post] 사용자 {} 상태 트러블슈팅 문서 개수 조회: userId={}, count={}",
			(status == null ? "ALL" : status), userId, posts.size());
		return posts;
	}

	public Page<Post> getAllTroubles(Long userId, Pageable pageable) {
		Page<Post> page = postRepository.findAllByUser_Id(userId, pageable);
		log.info("[Post] 전체 트러블 조회: userId={}, total={}, page={}, size={}, elementsInPage={}",
			userId, page.getTotalElements(), page.getNumber(), page.getSize(), page.getNumberOfElements());
		return page;
	}

	public Page<Post> getAllTroublesOrderByStarRating(Long userId, Pageable pageable) {
		Page<Post> page = postRepository.findTroublesByUserOrderByStarRating(userId, pageable);
		log.info("[Post] 전체 트러블 조회 (중요도순): userId={}, total={}, page={}, size={}, elementsInPage={}",
			userId, page.getTotalElements(), page.getNumber(), page.getSize(), page.getNumberOfElements());
		return page;
	}

	public String findErrorTag(final Post post) {
		if (post == null) {
			throw new PostException(ErrorCode.MISSING_ERROR_TAG);
		}
		return post.getPostTags().stream()
			.filter(Objects::nonNull)
			.filter(postTag -> postTag.getTag() != null)
			.filter(postTag -> postTag.getTag().getTagType() == TagType.ERROR)
			.map(PostTag::getDisplayName)
			.findFirst()
			.orElseThrow(() -> new PostException(ErrorCode.MISSING_ERROR_TAG));
	}

	public List<String> findTechStackTags(final Post post) {
		if (post == null) {
			return List.of();
		}
		return post.getPostTags().stream()
			.filter(Objects::nonNull)
			.filter(postTag -> postTag.getTag() != null)
			.filter(postTag -> postTag.getTag().getTagType() == TagType.TECH_STACK)
			.map(PostTag::getDisplayName)
			.toList();
	}

	public List<String> findTopTechStackTags(final Post post) {
		if (post == null) {
			return List.of();
		}

		if (CollectionUtils.isEmpty(post.getPostTags())) {
			return List.of();
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(Objects::nonNull)
			.filter(tag -> tag.isSameType(TagType.TECH_STACK))
			.collect(Collectors.groupingBy(Tag::getName, Collectors.counting()))
			.entrySet().stream()
			.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
			.limit(3)
			.map(Map.Entry::getKey)
			.toList();
	}

	public List<Post> getProjectTroublesByStatus(
		final Long projectId,
		final SortType sort,
		final VisibilityType type,
		final PostViewFilter statusType
	) {
		Boolean visible = VisibilityType.of(type);
		List<PostStatus> statuses = statusType.toPostStatuses();

		List<Post> posts = (sort == SortType.IMPORTANT)
			? postRepository.findByProjectImportantWithStatuses(projectId, statuses, visible)
			: postRepository.findByProjectWithStatuses(projectId, statuses, visible);

		log.info("[Post] {} 트러블슈팅 문서 조회: projectId={}, postCount={}", statusType.getMessage(), projectId, posts.size());
		return posts;
	}

	public List<PostSummary> getSummarizedTroubles(
		final Long projectId,
		final SortType sort,
		final SummaryType summaryType
	) {
		SummaryType.validate(summaryType);
		SummaryType filterType = (summaryType == SummaryType.NONE) ? null : summaryType;

		List<PostSummary> posts = (sort == SortType.IMPORTANT)
			? postSummaryRepository.findByProjectSummarizedImportant(projectId, PostStatus.SUMMARIZED, filterType)
			: postSummaryRepository.findByProjectSummarized(projectId, PostStatus.SUMMARIZED, filterType,
			Sort.by(DESC, "createdAt", "id"));
		log.info("[Post] 요약완료된 트러블슈팅 문서 조회: postCount={}, summaryType={}", posts.size(), summaryType);
		return posts;
	}

	public Page<Post> getCommunityPosts(final Pageable pageable) {
		Page<Post> page = postRepository.getCommunityPosts(pageable);
		log.info("[Post] 커뮤니티 게시글 조회: total={}, page={}, size={}, elementsInPage={}", page.getTotalElements(),
			page.getNumber(), page.getSize(), page.getNumberOfElements());
		return page;
	}

	public List<Post> findByIds(final List<Long> postIds) {
		if (CollectionUtils.isEmpty(postIds)) {
			log.info("[Post] 최근 열람 DB 조회: requested=0, found=0");
			return List.of();
		}
		List<Post> posts = postRepository.findByIdIn(postIds);
		log.info("[Post] 최근 열람 DB 조회: requested={}, found={}", postIds.size(), posts.size());
		return posts;
	}

	public Post findDeletedPostById(final Long postId) {
		log.info("[Post] 삭제된 트러블슈팅 문서 조회: postId={}", postId);
		return postRepository.findDeletedPostById(postId)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}
}
