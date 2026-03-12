package troublog.backend.domain.trouble.converter;

import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;

@UtilityClass
public class ListConverter {

	public TroubleListResDto toAllTroubleListResDto(
		final Post post,
		final String errorTag,
		final List<String> techs,
		final List<PostSummaryResDto> summaries
	) {
		return TroubleListResDto.builder()
			.id(post.getId())
			.projectId(post.getProject().getId())
			.title(post.getTitle())
			.date(post.getCompletedAt() != null ? post.getCompletedAt() : post.getUpdatedAt())
			.status(String.valueOf(post.getStatus()))
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.imageUrl(post.getThumbnailUrl())
			.error(errorTag)
			.techs(techs)
			.isVisible(post.getIsVisible())
			.starRating(
				post.getStarRating() != null ? post.getStarRating().getValue() : null)
			.summaries(summaries)
			.build();
	}

	public static TroubleListResDto toAllSummarizedListResDto(
		final PostSummary postSummary,
		final String errorTag,
		final List<String> techs
	) {
		return TroubleListResDto.builder()
			.id(postSummary.getPost().getId())
			.postSummaryId(postSummary.getId())
			.projectId(postSummary.getPost().getProject().getId())
			.title(postSummary.getPost().getTitle())
			.date(postSummary.getCreatedAt() != null ? postSummary.getCreatedAt() : null)
			.status(String.valueOf(postSummary.getPost().getStatus()))
			.starRating(
				postSummary.getPost().getStarRating() != null ? postSummary.getPost().getStarRating().getValue() : null)
			.imageUrl(postSummary.getPost().getThumbnailUrl())
			.error(errorTag)
			.techs(techs)
			.isVisible(postSummary.getPost().getIsVisible())
			.summaryType(postSummary.getSummaryType().name())
			.build();
	}
}
