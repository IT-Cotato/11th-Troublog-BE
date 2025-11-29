package troublog.backend.domain.statistics.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "일일 활동량 DTO")
public record DailyCountResDto(

	@Schema(description = "날짜")
	LocalDate date,

	@Schema(description = "개수")
	long count
) {
}

