package troublog.backend.domain.trouble.converter;

import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;

@UtilityClass
public class ListConverter {

	public TroubleListResDto toAllTroubleListResDto(Post post) {
		List<PostSummaryResDto> summaries = List.of();

		if (post.getStatus() == PostStatus.SUMMARIZED) {
			summaries = post.getPostSummaries().stream()
				.map(PostSummaryConverter::toResponse)
				.toList();
		}

		return TroubleListResDto.builder()
			.id(post.getId())
			.projectId(post.getProject().getId())
			.title(post.getTitle())
			.date(post.getCompletedAt() != null ? post.getCompletedAt() : post.getUpdated_at())
			.status(String.valueOf(post.getStatus()))
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.imageUrl(post.getThumbnailUrl())
			.error(PostQueryFacade.findErrorTag(post))
			.techs(PostQueryFacade.findTopTechStackTags(post))
			.isVisible(post.getIsVisible())
			.starRating(
				post.getStarRating() != null ? post.getStarRating().getValue() : null)
			.summaries(summaries)
			.build();
	}

	public static TroubleListResDto toAllSummerizedListResDto(PostSummary postSummary) {
		return TroubleListResDto.builder()
			.id(postSummary.getPost().getId())
			.postSummaryId(postSummary.getId())
			.projectId(postSummary.getPost().getProject().getId())
			.title(postSummary.getPost().getTitle())
			.date(postSummary.getCreated_at() != null ? postSummary.getCreated_at() : null)
			.status(String.valueOf(postSummary.getPost().getStatus()))
			.starRating(
				postSummary.getPost().getStarRating() != null ? postSummary.getPost().getStarRating().getValue() : null)
			.imageUrl(postSummary.getPost().getThumbnailUrl())
			.error(PostQueryFacade.findErrorTag(postSummary.getPost()))
			.techs(PostQueryFacade.findTopTechStackTags(postSummary.getPost()))
			.isVisible(postSummary.getPost().getIsVisible())
			.summaryType(postSummary.getSummaryType().name())
			.build();
	}
}
