package troublog.backend.domain.trouble.dto.resquest;

import io.swagger.v3.oas.annotations.media.Schema;
import troublog.backend.domain.trouble.enums.ContentAuthorType;
import troublog.backend.domain.trouble.enums.ContentSummaryType;

public record ContentDto(

	@Schema(
		description = "컨텐츠 소제목 내용",
		example = "해결 과정"
	)
	String subTitle,

	@Schema(
		description = "컨텐츠 본문 내용",
		example = "Spring Boot 프로젝트 설정 중 발생한 문제와 해결 과정"
	)
	String body,

	@Schema(
		description = "컨텐츠 순서",
		example = "1"
	)
	int sequence,

	@Schema(
		description = "컨텐츠 작성자 유형",
		example = "USER",
		allowableValues = {"USER_WRITTEN", "AI_GENERATED"}
	)
	ContentAuthorType authorType,

	@Schema(
		description = "컨텐츠 요약 유형",
		example = "USER_WRITTEN",
		allowableValues = {"ISSUE_MANAGEMENT", "BLOG", "INTERVIEW", "RESUME"}
	)
	ContentSummaryType summaryType
) {
}
