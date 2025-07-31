package troublog.backend.domain.trouble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "트러블로그 문서 본문 내용 DTO")
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
	@Min(value = 1, message = "시퀸스 값은 최소 1 이상이여야 합니다.")
	@NotNull(message = "컨텐츠 순서는 null 값일 수 없습니다.")
	Integer sequence,

	@Schema(
		description = "컨텐츠 작성자 유형",
		example = "USER_WRITTEN",
		allowableValues = {"USER_WRITTEN", "AI_GENERATED"}
	)
	@NotBlank(message = "컨텐츠 작성자 유형은 빈칸일 수 없습니다.")
	String authorType,

	@Schema(
		description =  "컨텐츠 요약 유형 - AI_GENERATED 일때만 설정된 값, USER_WRITTEN은 NONE",
		defaultValue = "NONE",
		allowableValues = {"ISSUE_MANAGEMENT", "BLOG", "INTERVIEW", "RESUME"}
	)
	String summaryType
) {
}
