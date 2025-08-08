package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.dto.common.ExtractedContentDto;
import troublog.backend.domain.trouble.dto.request.common.ContentDto;
import troublog.backend.domain.trouble.dto.response.common.ContentInfoDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.enums.ContentAuthorType;
import troublog.backend.domain.trouble.enums.ContentSummaryType;

@UtilityClass
public class ContentConverter {
	public Content toEntity(ContentDto contentDto) {
		return Content.builder()
			.subTitle(contentDto.subTitle())
			.body(contentDto.body())
			.sequence(contentDto.sequence())
			.authorType(ContentAuthorType.from(contentDto.authorType()))
			.summaryType(ContentSummaryType.from(contentDto.summaryType()))
			.build();
	}

	public ContentInfoDto toResponse(Content content) {
		return ContentInfoDto.builder()
			.id(content.getId())
			.subTitle(content.getSubTitle())
			.body(content.getBody())
			.sequence(content.getSequence())
			.authorType(content.getAuthorType())
			.summaryType(content.getSummaryType())
			.build();
	}

	public ExtractedContentDto extractContent(Content content) {
		return ExtractedContentDto.builder()
			.body(content.getBody())
			.sequence(content.getSequence())
			.build();
	}
}
