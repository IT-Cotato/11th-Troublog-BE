package troublog.backend.domain.trouble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SummaryTypeReqDto (
	@Schema(
		description =  "컨텐츠 요약 유형",
		example = "ISSUE_MANAGEMENT",
		allowableValues = {"ISSUE_MANAGEMENT", "BLOG", "INTERVIEW", "RESUME"}
	)
	@NotBlank(message = "AI 요약 타입은 공백일 수 없습니다.")
	String type
) {
}