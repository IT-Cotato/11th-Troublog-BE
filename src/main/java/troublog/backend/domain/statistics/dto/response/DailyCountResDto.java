package troublog.backend.domain.statistics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "일일 활동량 DTO")
public record DailyCountResDto(

        @Schema(description = "날짜")
        LocalDate date,

        @Schema(description = "개수")
        long count
) {}

