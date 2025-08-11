package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
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
			.imageUrl(null) // 수정 필요
			.error(PostQueryFacade.findErrorTag(post))
			.techs(PostQueryFacade.findTopTechStackTags(post))
			.isVisible(post.getIsVisible())
			.summaryType(post.getIsSummaryCreated() ?
				String.valueOf(post.getContents().getFirst().getSummaryType()) : null)
			.build();
	}
}
