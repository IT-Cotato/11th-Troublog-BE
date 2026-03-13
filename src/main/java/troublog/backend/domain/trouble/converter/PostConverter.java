package troublog.backend.domain.trouble.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.CommunityPostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.PostCardResDto;
import troublog.backend.domain.trouble.dto.response.PostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.dto.response.common.ContentInfoDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.dto.response.PostCardUserInfoResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.global.common.util.JsonConverter;

@UtilityClass
public class PostConverter {
	private static final int DEFAULT_COUNT = 0;
	private static final boolean DEFAULT_SUMMARY_CREATED = false;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	public Post createWritingPost(PostReqDto postReqDto) {
		return createBasePost(postReqDto)
			.status(PostStatus.WRITING)
			.isVisible(postReqDto.isVisible())
			.starRating(StarRating.from(postReqDto.starRating()))
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
			.templateType(postReqDto.templateType())
			.checklistError(JsonConverter.toJson(postReqDto.checklistError()))
			.checklistReason(JsonConverter.toJson(postReqDto.checklistReason()));
	}

	public PostResDto toResponse(final Post post, final String errorTag, final List<String> postTags) {
		return PostResDto.builder()
			.id(post.getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.isVisible(post.getIsVisible())
			.isSummaryCreated(post.getIsSummaryCreated())
			.thumbnailUrl(post.getThumbnailUrl() != null ? post.getThumbnailUrl() : null)
			.postStatus(post.getStatus().getMessage())
			.starRating(post.getStarRating() != null ? post.getStarRating().name() : null)
			.templateType(post.getTemplateType() != null ? post.getTemplateType().name() : null)
			.checklistError(JsonConverter.toList(post.getChecklistError()))
			.checklistReason(JsonConverter.toList(post.getChecklistReason()))
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.userInfo(UserConverter.toPostCardUserInfoResDto(post.getUser()))
			.projectId(post.getProject().getId())
			.errorTag(errorTag)
			.postTags(postTags)
			.contents(toContentResponses(post.getContents()))
			.thumbnailUrl(post.getThumbnailUrl())
			.build();
	}

	public PostDetailsResDto toPostDetailsResponse(
		final UserInfoResDto userInfoResDto,
		final Post post,
		final boolean liked,
		final String errorTag,
		final List<String> postTags
	) {
		return PostDetailsResDto.builder()
			.userInfoResDto(userInfoResDto)
			.postId(post.getId())
			.projectId(post.getProject().getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.liked(liked)
			.isVisible(post.getIsVisible())
			.isSummaryCreated(post.getIsSummaryCreated())
			.thumbnailImageUrl(post.getThumbnailUrl() != null ? post.getThumbnailUrl() : null)
			.postStatus(post.getStatus() != null ? String.valueOf(post.getStatus()) : null)
			.starRating(post.getStarRating() != null ? post.getStarRating().getValue() : null)
			.templateType(post.getTemplateType() != null ? post.getTemplateType().toString() : null)
			.checklistError(JsonConverter.toList(post.getChecklistError()))
			.checklistReason(JsonConverter.toList(post.getChecklistReason()))
			.errorTag(errorTag)
			.postTags(postTags)
			.contents(toContentResponses(post.getContents()))
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.completedAt(post.getCompletedAt() != null ? post.getCompletedAt().format(DATE_FORMATTER) : null)
			.build();
	}

	public PostCardResDto toCommunityListResponse(
		final PostCardUserInfoResDto postCardUserInfoResDto,
		final Post post,
		final String errorTag,
		final List<String> postTags
	) {
		return PostCardResDto.builder()
			.postCardUserInfoResDto(postCardUserInfoResDto)
			.id(post.getId())
			.title(post.getTitle())
			.thumbnailUrl(post.getThumbnailUrl())
			.errorTag(errorTag)
			.postTags(postTags)
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.completedAt(post.getCompletedAt() == null ? null : post.getCompletedAt().format(DATE_FORMATTER))
			.build();
	}

	public CommunityPostDetailsResDto toCommunityPostDetailsResponse(
		final UserInfoResDto userInfoResDto,
		final Post post,
		final boolean liked,
		final String errorTag,
		final List<String> postTags
	) {
		return CommunityPostDetailsResDto.builder()
			.userInfoResDto(userInfoResDto)
			.postId(post.getId())
			.projectId(post.getProject().getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.liked(liked)
			.thumbnailImageUrl(post.getThumbnailUrl() != null ? post.getThumbnailUrl() : null)
			.postStatus(post.getStatus() != null ? String.valueOf(post.getStatus()) : null)
			.templateType(post.getTemplateType() != null ? post.getTemplateType().toString() : null)
			.checklistError(JsonConverter.toList(post.getChecklistError()))
			.checklistReason(JsonConverter.toList(post.getChecklistReason()))
			.errorTag(errorTag)
			.postTags(postTags)
			.contents(toContentResponses(post.getContents()))
			.createdAt(post.getCreatedAt())
			.updatedAt(post.getUpdatedAt())
			.completedAt(post.getCompletedAt() != null ? post.getCompletedAt().format(DATE_FORMATTER) : null)
			.build();
	}

	private List<ContentInfoDto> toContentResponses(final List<Content> contents) {
		if (CollectionUtils.isEmpty(contents)) {
			return List.of();
		}
		return ContentConverter.toResponseList(contents);
	}
}
