package troublog.backend.domain.trouble.converter;

import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.trouble.dto.request.common.SummaryContentDto;
import troublog.backend.domain.trouble.dto.response.common.SummaryContentInfoDto;
import troublog.backend.domain.trouble.entity.SummaryContent;

@UtilityClass
public class SummaryContentConverter {

	public List<SummaryContent> toEntityList(SummarizedResDto summarizedResDto) {
		return summarizedResDto.contentDtoList().stream()
			.map(SummaryContentConverter::toEntity)
			.toList();
	}

	public SummaryContent toEntity(SummaryContentDto summaryContentDto) {
		return SummaryContent.builder()
			.subTitle(summaryContentDto.subTitle())
			.body(summaryContentDto.body())
			.sequence(summaryContentDto.sequence())
			.build();
	}

	public List<SummaryContentInfoDto> toResponseList(List<SummaryContent> summaryContents) {
		return summaryContents.stream()
			.map(SummaryContentConverter::toResponse)
			.toList();
	}

	public SummaryContentInfoDto toResponse(SummaryContent summaryContent) {
		return SummaryContentInfoDto.builder()
			.id(summaryContent.getId())
			.subTitle(summaryContent.getSubTitle())
			.body(summaryContent.getBody())
			.sequence(summaryContent.getSequence())
			.build();
	}
}
