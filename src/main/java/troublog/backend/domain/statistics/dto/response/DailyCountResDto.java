package troublog.backend.domain.statistics.dto.response;

import java.time.LocalDate;

public record DailyCountResDto(LocalDate date, long count) {
}

