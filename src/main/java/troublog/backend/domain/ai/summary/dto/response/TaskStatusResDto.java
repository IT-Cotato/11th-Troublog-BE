package troublog.backend.domain.ai.summary.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;

@Builder
@Schema(description = "AI 요약 작업 상태 응답 DTO")
public record TaskStatusResDto(
	@Schema(
		description = "작업 고유 식별자",
		example = "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6"
	)
	String taskId,

	@Schema(
		description = "사용자 ID",
		example = "2"
	)
	Long userId,

	@Schema(
		description = "작업 상태",
		example = "STARTED",
		allowableValues = {"PENDING", "STARTED", "PREPROCESSING", "ANALYZING", "POSTPROCESSING", "COMPLETED", "FAILED",
			"CANCELLED"}
	)
	SummaryStatus status,

	@Schema(
		description = "현재 작업 단계 메시지",
		example = "트러블슈팅 문서 분석을 시작했어요"
	)
	String currentStep,

	@Schema(
		description = "작업 진행률 (퍼센트)",
		example = "10",
		minimum = "0",
		maximum = "100"
	)
	Integer progress,

	@Schema(
		description = "AI 분석 완료 결과 (작업 완료 시에만 제공)"
	)
	SummarizedResDto result,

	@Schema(
		description = "작업 시작 일시",
		example = "2025-01-08T14:30:00"
	)
	LocalDateTime createdAt,

	@Schema(
		description = "작업 완료 일시 (작업 완료/실패 시에만 제공)",
		example = "2025-01-08T14:32:30"
	)
	LocalDateTime completedAt
) {
}
