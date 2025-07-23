package troublog.backend.domain.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import troublog.backend.domain.statistics.dto.response.DailyCountResDto;
import troublog.backend.domain.statistics.dto.response.StatsResDto;
import troublog.backend.domain.statistics.repository.StatisticsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public List<DailyCountResDto> getDailyPostStats(Long userId) {
        LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = LocalDate.now();

        /**
         * return postRepository.findDailyPostCountByUser(
         *             userId,
         *             start.atStartOfDay(),
         *             end.atTime(LocalTime.MAX)
         *         );
         */
    }

    public List<StatsResDto> getTopTagStats(Long userId) {
    }

    public List<StatsResDto> getTopErrorStats(Long userId) {
    }

    public List<StatsResDto> getSummaryStats(Long userId) {
    }
}
