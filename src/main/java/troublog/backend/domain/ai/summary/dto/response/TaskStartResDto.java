package troublog.backend.domain.ai.summary.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;

@Builder
@Schema(description = "AI 요약 작업 시작 응답 DTO")
public record TaskStartResDto(
	@Schema(
		description = "생성된 작업 ID(UUID)",
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
		example = "PENDING"
	)
	SummaryStatus status,

	@Schema(
		description = "상태 메시지",
		example = "요약 작업이 대기열에 등록되었어요! 잠시만 기다려주세요"
	)
	String message,

	@Schema(
		description = "작업 생성 시간",
		example = "2025-06-29T10:30:00"
	)
	LocalDateTime createdAt
) {
}
