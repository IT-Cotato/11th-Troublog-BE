package troublog.backend.domain.trouble.service.facade.query;

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
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.converter.ListConverter;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.converter.PostSummaryConverter;
import troublog.backend.domain.trouble.dto.response.CombineResDto;
import troublog.backend.domain.trouble.dto.response.CommunityPostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.PostCardResDto;
import troublog.backend.domain.trouble.dto.response.PostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.dto.response.common.ContentInfoDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagCategory;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.trouble.service.facade.command.RecentPostCommandFacade;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.LikeQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.service.query.PostSummaryQueryService;
import troublog.backend.domain.trouble.service.query.RecentPostQueryService;
import troublog.backend.domain.trouble.service.query.TagQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;
import troublog.backend.domain.user.dto.response.PostCardUserInfoResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.domain.user.service.UserFacade;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryFacade {

	private final PostQueryService postQueryService;
	private final TagQueryService tagQueryService;
	private final UserFacade userFacade;
	private final LikeQueryService likeQueryService;
	private final RecentPostCommandFacade recentPostCommandFacade;
	private final RecentPostQueryService recentPostQueryService;
	private final PostSummaryQueryService postSummaryQueryService;

	public Post findPostEntityById(Long id, Long userId) {
		Post post = postQueryService.findById(id);
		PostFactory.validateAuthorized(userId, post);
		return post;
	}

	public PostDetailsResDto findPostById(Long id, Long userId) {
		Post post = postQueryService.findById(id);
		PostFactory.validateAuthorized(userId, post);
		UserInfoResDto userInfo = userFacade.getUserInfo(post.getUser().getId(), userId);
		boolean liked = likeQueryService.findByUserAndPost(userId, post.getId()).isPresent();
		return PostConverter.toPostDetailsResponse(userInfo, post, liked);
	}

	public static List<ContentInfoDto> findContents(Post post) {
		if (post.getContents() == null || post.getContents().isEmpty()) {
			return List.of();
		}
		return ContentConverter.toResponseList(post.getContents());
	}

	public CombineResDto findPostDetailsWithSummaryById(Long userId, Long postId, Long summaryId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateAuthorized(userId, post);

		PostSummary postSummary = postSummaryQueryService.findById(summaryId);
		PostValidator.validateSummaryBelongsToUser(userId, postSummary);

		if (!postSummary.getPost().getId().equals(postId)) {
			throw new PostException(ErrorCode.POST_SUMMARY_NOT_FOUND);
		}

		return CombineResDto.builder()
			.postResDto(PostConverter.toResponse(post))
			.postSummaryResDto(PostSummaryConverter.toResponse(postSummary))
			.build();
	}

	public List<PostResDto> findAllNotDeletedPosts() {
		List<Post> posts = postQueryService.findAllNotDeletedPosts();
		return PostConverter.toResponseList(posts);
	}

	public List<PostResDto> findDeletedPosts() {
		List<Post> posts = postQueryService.findAllDeletedPosts();
		return PostConverter.toResponseList(posts);
	}

	public List<String> findPostTagsByCategory(String category) {
		TagCategory tagCategory = TagCategory.from(category);
		List<Tag> techStacks = tagQueryService.findTechStackTagsByCategory(tagCategory);
		return techStacks.stream()
			.map(Tag::getName)
			.toList();
	}

	public List<String> findPostTagsByName(String name) {
		List<Tag> techStacks = tagQueryService.findTechStackTagContainsName(name);
		return techStacks.stream()
			.map(Tag::getName)
			.toList();
	}

	public Page<PostCardResDto> searchUserPostByKeyword(Long userId, String keyword, Pageable pageable) {
		Page<Post> posts = postQueryService.searchUserPostByKeyword(userId, keyword, pageable);
		Set<Long> userIds = posts.getContent().stream()
			.map(post -> post.getUser().getId())
			.collect(Collectors.toSet());

		Map<Long, PostCardUserInfoResDto> userInfoMap = userFacade.getUserInfoMap(userIds);

		return posts.map(post -> {
			PostCardUserInfoResDto userInfo = userInfoMap.get(post.getUser().getId());
			return PostConverter.toCommunityListResponse(userInfo, post);
		});
	}

	public Page<PostResDto> searchPostByKeyword(String keyword, Pageable pageable) {
		Page<Post> posts = postQueryService.searchPostByKeyword(keyword, pageable);
		return posts.map(PostConverter::toResponse);
	}

	public Page<TroubleListResDto> getAllTroubles(Long userId, Pageable pageable) {
		Page<Post> posts = isSortByStarRating(pageable)
			? postQueryService.getAllTroublesOrderByStarRating(userId, pageable)
			: postQueryService.getAllTroubles(userId, pageable);
		return posts.map(ListConverter::toAllTroubleListResDto);
	}

	public CommunityPostDetailsResDto findCommunityPostDetailsById(Long userId, Long postId) {
		Post post = postQueryService.findById(postId);
		PostValidator.validateVisibility(post);
		UserInfoResDto userInfo = userFacade.getUserInfo(post.getUser().getId(), userId);
		boolean liked = likeQueryService.findByUserAndPost(userId, postId).isPresent();
		recentPostCommandFacade.recordPostView(userId, postId);
		return PostConverter.toCommunityPostDetailsResponse(userInfo, post, liked);
	}

	public Page<PostCardResDto> getCommunityPosts(Pageable pageable) {
		Page<Post> posts = postQueryService.getCommunityPosts(pageable);

		Set<Long> userIds = posts.getContent().stream()
			.map(post -> post.getUser().getId())
			.collect(Collectors.toSet());

		Map<Long, PostCardUserInfoResDto> userInfoMap = userFacade.getUserInfoMap(userIds);

		return posts.map(post -> {
			PostCardUserInfoResDto userInfo = userInfoMap.get(post.getUser().getId());
			return PostConverter.toCommunityListResponse(userInfo, post);
		});
	}

	public Pageable getPageable(int page, int size) {
		return PageRequest.of(Math.max(0, page - 1), size);
	}

	public PageRequest getPageableWithSorting(int page, int size, String sortBy) {
		return PageRequest.of(Math.max(0, page - 1), size, getSortByCriteria(sortBy));
	}

	private Sort getSortByCriteria(String sortBy) {
		return switch (sortBy.toLowerCase()) {
			case "likes" -> Sort.by(Sort.Direction.DESC, "likeCount");
			case "important" -> Sort.by(Sort.Direction.DESC, "starRating");
			case "latest" -> Sort.by(Sort.Direction.DESC, "completedAt", "id");
			default -> throw new PostException(ErrorCode.INVALID_VALUE);
		};
	}

	public static String findErrorTag(Post post) {
		if (post.getPostTags() == null || post.getPostTags().isEmpty()) {
			return null;
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(tag -> tag != null && tag.getTagType() == TagType.ERROR)
			.map(Tag::getName)
			.findFirst()
			.orElse(null);
	}

	public static List<String> findTechStackTags(Post post) {
		if (post.getPostTags() == null || post.getPostTags().isEmpty()) {
			return List.of();
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(tag -> tag != null && tag.getTagType() == TagType.TECH_STACK)
			.map(Tag::getName)
			.toList();
	}

	public static List<String> findTopTechStackTags(Post post) {
		if (post.getPostTags() == null || post.getPostTags().isEmpty()) {
			return List.of();
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(t -> t != null && t.getTagType() == TagType.TECH_STACK)
			.collect(Collectors.groupingBy(Tag::getName, Collectors.counting()))
			.entrySet().stream()
			.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
			.limit(3)
			.map(Map.Entry::getKey)
			.toList();
	}

	public Page<PostResDto> getRecentlyViewedPosts(Long userId, Pageable pageable) {
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
			.map(PostConverter::toResponse)
			.toList();

		return new PageImpl<>(content, pageable, total);
	}

	private boolean isSortByStarRating(Pageable pageable) {
		return pageable.getSort().getOrderFor("starRating") != null;
	}
}
