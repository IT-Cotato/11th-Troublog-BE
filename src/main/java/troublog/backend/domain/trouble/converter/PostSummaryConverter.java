package troublog.backend.domain.trouble.converter;

import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.dto.response.common.SummaryContentInfoDto;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.SummaryContent;

@UtilityClass
public class PostSummaryConverter {

	public PostSummary toEntity(SummaryTask summaryTask) {
		return PostSummary.builder()
			.summaryType(summaryTask.getSummaryType())
			.build();
	}

	public PostSummaryResDto toResponse(
		final PostSummary postSummary,
		final String errorTag,
		final List<String> postTags
	) {
		return PostSummaryResDto.builder()
			.postId(postSummary.getPost().getId())
			.title(postSummary.getPost().getTitle())
			.userId(postSummary.getPost().getUser().getId())
			.projectId(postSummary.getPost().getProject().getId())
			.errorTag(errorTag)
			.postTags(postTags)
			.summaryId(postSummary.getId())
			.summaryType(postSummary.getSummaryType().name())
			.summaryCreatedAt(postSummary.getCreatedAt())
			.summaryContents(toSummaryContentResponses(postSummary.getSummaryContents()))
			.build();
	}

	private List<SummaryContentInfoDto> toSummaryContentResponses(final List<SummaryContent> summaryContents) {
		if (summaryContents == null) {
			return List.of();
		}
		return SummaryContentConverter.toResponseList(summaryContents);
	}
}
