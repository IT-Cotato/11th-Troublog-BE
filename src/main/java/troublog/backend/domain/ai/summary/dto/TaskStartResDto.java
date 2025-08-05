package troublog.backend.domain.ai.summary.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskStartResDto(
    @Schema(
        description = "생성된 작업 고유 식별자",
        example = "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6"
	)
    String taskId,

    @Schema(
        description = "작업 생성 시간",
        example = "2025-06-29T10:30:00"
    )
    LocalDateTime createdAt,

    @Schema(
        description = "작업 상태 확인 URL",
        example = "/troubles/summary/status/a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6"
    )
    String statusUrl
) {
}
