package troublog.backend.domain.report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "신고 응답 DTO")
public record ReportResDto(
	@Schema(description = "신고 ID")
	Long reportId
) {
}
