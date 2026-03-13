package troublog.backend.domain.trouble.service.facade;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.ListConverter;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.converter.PostSummaryConverter;
import troublog.backend.domain.trouble.dto.response.CombineResDto;
import troublog.backend.domain.trouble.dto.response.CommunityPostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.PostCardResDto;
import troublog.backend.domain.trouble.dto.response.PostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.PostViewFilter;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.LikeQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.service.query.PostSummaryQueryService;
import troublog.backend.domain.trouble.service.query.RecentPostQueryService;
import troublog.backend.domain.trouble.service.query.TagQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;
import troublog.backend.domain.user.dto.response.PostCardUserInfoResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.domain.user.service.facade.UserFacadeService;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryFacadeService {

	private final PostQueryService postQueryService;
	private final TagQueryService tagQueryService;
	private final UserFacadeService userFacadeService;
	private final LikeQueryService likeQueryService;
	private final RecentPostCommandFacadeService recentPostCommandFacadeService;
	private final RecentPostQueryService recentPostQueryService;
	private final PostSummaryQueryService postSummaryQueryService;

	public Post findPostEntityById(final Long postId, final Long userId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, post);
		return post;
	}

	public PostDetailsResDto findPostById(final Long postId, final Long userId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, post);
		UserInfoResDto userInfo = userFacadeService.getUserInfo(post.getUser().getId(), userId);
		boolean liked = likeQueryService.findByUserAndPost(userId, post.getId()).isPresent();
		return toPostDetailsResponse(userInfo, post, liked);
	}

	public CombineResDto findPostDetailsWithSummaryById(final Long userId, final Long postId, final Long summaryId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, post);

		PostSummary postSummary = postSummaryQueryService.findById(summaryId);
		PostValidator.validateSummaryBelongsToUser(userId, postSummary);

		if (!postSummary.getPost().getId().equals(postId)) {
			throw new PostException(ErrorCode.POST_SUMMARY_NOT_FOUND);
		}

		return CombineResDto.builder()
			.postResDto(toPostResponse(post))
			.postSummaryResDto(toPostSummaryResponse(postSummary))
			.build();
	}

	public List<PostResDto> findAllNotDeletedPosts() {
		List<Post> posts = postQueryService.findAllNotDeletedPosts();
		return posts.stream()
			.map(this::toPostResponse)
			.toList();
	}

	public List<PostResDto> findDeletedPosts() {
		List<Post> posts = postQueryService.findAllDeletedPosts();
		return posts.stream()
			.map(this::toPostResponse)
			.toList();
	}

	public List<String> findPostTagsByName(final String keyword) {
		List<Tag> techStacks = tagQueryService.findTechStackTagContainsName(keyword);
		return techStacks.stream()
			.map(Tag::getName)
			.toList();
	}

	public Page<PostResDto> searchPostByKeyword(final String keyword, final Pageable pageable) {
		Page<Post> posts = postQueryService.searchPostByKeyword(keyword, pageable);
		return posts.map(this::toPostResponse);
	}

	public Page<TroubleListResDto> getAllTroubles(final Long userId, final Pageable pageable) {
		Page<Post> posts = isSortByStarRating(pageable)
			? postQueryService.getAllTroublesOrderByStarRating(userId, pageable)
			: postQueryService.getAllTroubles(userId, pageable);
		return posts.map(this::toTroubleListResponse);
	}

	public List<TroubleListResDto> findProjectTroublesByStatus(
		final Long projectId,
		final SortType sort,
		final VisibilityType visibility,
		final PostViewFilter statusType
	) {
		List<Post> posts = postQueryService.getProjectTroublesByStatus(projectId, sort, visibility, statusType);
		return posts.stream()
			.map(this::toTroubleListResponse)
			.toList();
	}

	public List<TroubleListResDto> findProjectSummarizedTroubles(
		final Long projectId,
		final SortType sort,
		final SummaryType summaryType
	) {
		List<PostSummary> postSummaries = postQueryService.getSummarizedTroubles(projectId, sort, summaryType);
		return postSummaries.stream()
			.map(this::toSummarizedTroubleListResponse)
			.toList();
	}

	public CommunityPostDetailsResDto findCommunityPostDetailsById(final Long userId, final Long postId) {
		Post post = postQueryService.findById(postId);
		PostValidator.validateVisibility(post);
		UserInfoResDto userInfo = userFacadeService.getUserInfo(post.getUser().getId(), userId);
		boolean liked = likeQueryService.findByUserAndPost(userId, postId).isPresent();
		recentPostCommandFacadeService.recordPostView(userId, postId);
		return toCommunityPostDetailsResponse(userInfo, post, liked);
	}

	public Page<PostCardResDto> searchUserPostByKeyword(
		final Long userId, final String keyword,
		final Pageable pageable
	) {
		Page<Post> posts = postQueryService.searchUserPostByKeyword(userId, keyword, pageable);
		Set<Long> userIds = getDeduplicationUserId(posts);
		return convertToPostCardsWithUserInfo(userIds, posts);
	}

	public Page<PostCardResDto> getCommunityPosts(final Pageable pageable) {
		Page<Post> posts = postQueryService.getCommunityPosts(pageable);
		Set<Long> userIds = getDeduplicationUserId(posts);
		return convertToPostCardsWithUserInfo(userIds, posts);
	}

	private Set<Long> getDeduplicationUserId(final Page<Post> posts) {
		return posts.getContent().stream()
			.map(post -> post.getUser().getId())
			.collect(Collectors.toSet());
	}

	private Page<PostCardResDto> convertToPostCardsWithUserInfo(final Set<Long> userIds, final Page<Post> posts) {
		Map<Long, PostCardUserInfoResDto> userInfoMap = userFacadeService.getUserInfoMap(userIds);

		return posts.map(post -> {
			PostCardUserInfoResDto userInfo = userInfoMap.get(post.getUser().getId());
			return toCommunityListResponse(userInfo, post);
		});
	}

	public Pageable getPageable(final int page, final int size) {
		return PageRequest.of(Math.max(0, page - 1), size);
	}

	public PageRequest getPageableWithSorting(final int page, final int size, final String sortBy) {
		return PageRequest.of(Math.max(0, page - 1), size, getSortByCriteria(sortBy));
	}

	private Sort getSortByCriteria(final String sortBy) {
		return switch (sortBy.toLowerCase()) {
			case "likes" -> Sort.by(Sort.Direction.DESC, "likeCount");
			case "important" -> Sort.by(Sort.Direction.DESC, "starRating");
			case "latest" -> Sort.by(Sort.Direction.DESC, "completedAt", "id");
			default -> throw new PostException(ErrorCode.INVALID_VALUE);
		};
	}

	public Page<PostResDto> getRecentlyViewedPosts(final Long userId, final Pageable pageable) {
		long offset = pageable.getOffset();
		int size = pageable.getPageSize();

		long total = recentPostQueryService.getRecentlyViewedCount(userId);
		if (total == 0 || offset >= total) {
			return Page.empty(pageable);
		}

		List<Long> recentIds = recentPostQueryService.getRecentlyViewedPostIds(userId, offset, size);
		if (recentIds.isEmpty()) {
			return Page.empty(pageable);
		}

		List<Post> posts = postQueryService.findByIds(recentIds);

		Map<Long, Post> postMap = posts.stream()
			.collect(Collectors.toMap(Post::getId, p -> p, (a, b) -> a));

		List<PostResDto> content = recentIds.stream()
			.map(postMap::get)
			.filter(Objects::nonNull)
			.map(this::toPostResponse)
			.toList();

		return new PageImpl<>(content, pageable, total);
	}

	private boolean isSortByStarRating(final Pageable pageable) {
		return pageable.getSort().getOrderFor("starRating") != null;
	}

	private PostResDto toPostResponse(final Post post) {
		return PostConverter.toResponse(
			post,
			postQueryService.findErrorTag(post),
			postQueryService.findTechStackTags(post)
		);
	}

	private PostDetailsResDto toPostDetailsResponse(
		final UserInfoResDto userInfoResDto,
		final Post post,
		final boolean liked
	) {
		return PostConverter.toPostDetailsResponse(
			userInfoResDto,
			post,
			liked,
			postQueryService.findErrorTag(post),
			postQueryService.findTechStackTags(post)
		);
	}

	private CommunityPostDetailsResDto toCommunityPostDetailsResponse(
		final UserInfoResDto userInfoResDto,
		final Post post,
		final boolean liked
	) {
		return PostConverter.toCommunityPostDetailsResponse(
			userInfoResDto,
			post,
			liked,
			postQueryService.findErrorTag(post),
			postQueryService.findTechStackTags(post)
		);
	}

	private PostCardResDto toCommunityListResponse(
		final PostCardUserInfoResDto postCardUserInfoResDto,
		final Post post
	) {
		return PostConverter.toCommunityListResponse(
			postCardUserInfoResDto,
			post,
			postQueryService.findErrorTag(post),
			postQueryService.findTopTechStackTags(post)
		);
	}

	private TroubleListResDto toTroubleListResponse(final Post post) {
		List<PostSummaryResDto> summaries = List.of();

		if (post.getStatus() == PostStatus.SUMMARIZED) {
			summaries = post.getPostSummaries().stream()
				.map(this::toPostSummaryResponse)
				.toList();
		}

		return ListConverter.toAllTroubleListResDto(
			post,
			postQueryService.findErrorTag(post),
			postQueryService.findTopTechStackTags(post),
			summaries
		);
	}

	private TroubleListResDto toSummarizedTroubleListResponse(final PostSummary postSummary) {
		return ListConverter.toAllSummarizedListResDto(
			postSummary,
			postQueryService.findErrorTag(postSummary.getPost()),
			postQueryService.findTopTechStackTags(postSummary.getPost())
		);
	}

	private PostSummaryResDto toPostSummaryResponse(final PostSummary postSummary) {
		return PostSummaryConverter.toResponse(
			postSummary,
			postQueryService.findErrorTag(postSummary.getPost()),
			postQueryService.findTechStackTags(postSummary.getPost())
		);
	}
}
