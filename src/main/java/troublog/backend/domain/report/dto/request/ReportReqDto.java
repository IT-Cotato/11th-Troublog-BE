package troublog.backend.domain.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import troublog.backend.domain.report.enums.ReportTargetType;
import troublog.backend.domain.report.enums.ReportType;

@Builder
@Schema(description = "신고 요청 DTO")
public record ReportReqDto(
	@NotNull
	@Schema(description = "신고자 ID")
	Long reportingUserId,

	@NotNull
	@Schema(description = "피신고자 ID")
	Long reportedUserId,

	@NotNull
	@Schema(description = "신고 대상 타입", allowableValues = {"POST", "COMMENT"})
	ReportTargetType targetType,

	@NotNull
	@Schema(description = "신고 대상 타입 ID")
	Long targetId,

	@NotNull
	@Schema(description = "신고 유형", allowableValues = {
		"SPAM", "INFO", "BLAME", "PRIVACY", "SUBJECT", "COPYRIGHT"
	})
	ReportType reportType
) {
}
