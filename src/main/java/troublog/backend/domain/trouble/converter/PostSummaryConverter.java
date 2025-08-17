package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;
import troublog.backend.domain.trouble.service.facade.query.PostSummaryQueryFacade;

@UtilityClass
public class PostSummaryConverter {

	public PostSummary toEntity(SummaryTask summaryTask) {
		return PostSummary.builder()
			.summaryType(summaryTask.getSummaryType())
			.build();
	}

	public PostSummaryResDto toResponse(PostSummary postSummary) {
		return PostSummaryResDto.builder()
			.postId(postSummary.getPost().getId())
			.title(postSummary.getPost().getTitle())
			.userId(postSummary.getPost().getUser().getId())
			.projectId(postSummary.getPost().getProject().getId())
			.errorTag(PostQueryFacade.findErrorTag(postSummary.getPost()))
			.postTags(PostQueryFacade.findTechStackTags(postSummary.getPost()))
			.summaryId(postSummary.getId())
			.summaryType(postSummary.getSummaryType().name())
			.summaryCreatedAt(postSummary.getCreated_at())
			.summaryContents(PostSummaryQueryFacade.findSummaryContents(postSummary))
			.build();
	}

}
