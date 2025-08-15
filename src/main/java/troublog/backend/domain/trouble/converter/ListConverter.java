package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;

@UtilityClass
public class ListConverter {

	public TroubleListResDto toAllTroubleListResDto(Post post) {
		return TroubleListResDto.builder()
			.id(post.getId())
			.projectId(post.getProject().getId())
			.title(post.getTitle())
			.date(post.getCompletedAt() != null ? post.getCompletedAt() : post.getUpdated_at())
			.status(String.valueOf(post.getStatus()))
			.starRating(post.getStarRating() != null ? post.getStarRating().getValue() : null)
			.imageUrl(post.getThumbnailUrl())
			.error(PostQueryFacade.findErrorTag(post))
			.techs(PostQueryFacade.findTopTechStackTags(post))
			.isVisible(post.getIsVisible())
			// .summaryType(PostValidator.isValidSummaryContent(post) ?
			// 	SummaryType.getName(post.getContents().getFirst().getSummaryType()) : null)
			.build();
	}

	public static TroubleListResDto toAllSummerizedListResDto(PostSummary postSummary) {
		return TroubleListResDto.builder()
			.id(postSummary.getPost().getId())
			.summaryPostId(postSummary.getId())
			.projectId(postSummary.getPost().getProject().getId())
			.title(postSummary.getPost().getTitle())
			.date(postSummary.getCreated_at() != null ? postSummary.getCreated_at() : null)
			.status(String.valueOf(postSummary.getPost().getStatus()))
			.starRating(postSummary.getPost().getStarRating() != null ? postSummary.getPost().getStarRating().getValue() : null)
			.imageUrl(postSummary.getPost().getThumbnailUrl())
			.error(PostQueryFacade.findErrorTag(postSummary.getPost()))
			.techs(PostQueryFacade.findTopTechStackTags(postSummary.getPost()))
			.isVisible(postSummary.getPost().getIsVisible())
			.build();
	}
}
