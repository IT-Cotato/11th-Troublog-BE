package troublog.backend.domain.trouble.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.CommunityListResDto;
import troublog.backend.domain.trouble.dto.response.CommunityPostResDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.dto.response.PostCardUserInfoResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.global.common.util.JsonConverter;

@UtilityClass
public class PostConverter {
	private static final int DEFAULT_COUNT = 0;
	private static final boolean DEFAULT_VISIBLE = false;
	private static final boolean DEFAULT_SUMMARY_CREATED = false;
	private static final boolean DEFAULT_DELETE_STATUS = false;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	public Post createWritingPost(PostReqDto postReqDto) {
		return createBasePost(postReqDto)
			.status(PostStatus.WRITING)
			.isVisible(DEFAULT_VISIBLE)
			.isSummaryCreated(DEFAULT_SUMMARY_CREATED)
			.build();
	}

	public Post createCompletedPost(PostReqDto postReqDto) {
		return createBasePost(postReqDto)
			.introduction(postReqDto.introduction())
			.isVisible(postReqDto.isVisible())
			.isSummaryCreated(DEFAULT_SUMMARY_CREATED)
			.status(PostStatus.COMPLETED)
			.starRating(StarRating.from(postReqDto.starRating()))
			.thumbnailUrl(postReqDto.thumbnailImageUrl())
			.completedAt(LocalDateTime.now())
			.build();
	}

	public Post createSummarizedPost(PostReqDto postReqDto) {
		return createBasePost(postReqDto)
			.introduction(postReqDto.introduction())
			.isVisible(postReqDto.isVisible())
			.isSummaryCreated(true)
			.status(PostStatus.SUMMARIZED)
			.starRating(StarRating.from(postReqDto.starRating()))
			.thumbnailUrl(postReqDto.thumbnailImageUrl())
			.completedAt(LocalDateTime.now())
			.build();
	}

	private Post.PostBuilder createBasePost(PostReqDto postReqDto) {
		return Post.builder()
			.title(postReqDto.title())
			.commentCount(DEFAULT_COUNT)
			.likeCount(DEFAULT_COUNT)
			.isDeleted(DEFAULT_DELETE_STATUS)
			.checklistError(JsonConverter.toJson(postReqDto.checklistError()))
			.checklistReason(JsonConverter.toJson(postReqDto.checklistReason()));
	}

	public PostResDto toResponse(Post post) {
		return PostResDto.builder()
			.id(post.getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.isVisible(post.getIsVisible())
			.isSummaryCreated(post.getIsSummaryCreated())
			.postStatus(post.getStatus().getMessage())
			.starRating(post.getStarRating() != null ? post.getStarRating().name() : null)
			.checklistError(JsonConverter.toList(post.getChecklistError()))
			.checklistReason(JsonConverter.toList(post.getChecklistReason()))
			.createdAt(post.getCreated_at())
			.updatedAt(post.getUpdated_at())
			.userInfo(UserConverter.toPostCardUserInfoResDto(post.getUser()))
			.projectId(post.getProject().getId())
			.errorTag(PostQueryFacade.findErrorTag(post))
			.postTags(PostQueryFacade.findTechStackTags(post))
			.contents(PostQueryFacade.findContents(post))
			.thumbnailUrl(post.getThumbnailUrl())
			.build();
	}

	public CommunityPostResDto toCommunityDetailsResponse(UserInfoResDto userInfoResDto, Post post, boolean liked) {
		return CommunityPostResDto.builder()
			.userInfoResDto(userInfoResDto)
			.id(post.getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.errorTag(PostQueryFacade.findErrorTag(post))
			.postTags(PostQueryFacade.findTechStackTags(post))
			.contents(PostQueryFacade.findContents(post))
			.likeCount(post.getLikeCount())
			.liked(liked)
			.commentCount(post.getCommentCount())
			.completedAt(post.getCompletedAt() == null ? null : post.getCompletedAt().format(DATE_FORMATTER))
			.build();
	}

	public CommunityListResDto toCommunityListResponse(PostCardUserInfoResDto postCardUserInfoResDto, Post post) {
		return CommunityListResDto.builder()
			.postCardUserInfoResDto(postCardUserInfoResDto)
			.id(post.getId())
			.title(post.getTitle())
			.thumbnailUrl(post.getThumbnailUrl())
			.errorTag(PostQueryFacade.findErrorTag(post))
			.postTags(PostQueryFacade.findTopTechStackTags(post))
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.completedAt(post.getCompletedAt() == null ? null : post.getCompletedAt().format(DATE_FORMATTER))
			.build();
	}

	public List<PostResDto> toResponseList(List<Post> posts) {
		return posts.stream()
			.map(PostConverter::toResponse)
			.toList();
	}
}