package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.resquest.ContentDto;
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
}
