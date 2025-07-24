package troublog.backend.domain.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import troublog.backend.domain.statistics.dto.response.DailyCountResDto;
import troublog.backend.domain.statistics.dto.response.StatsResDto;
import troublog.backend.domain.statistics.repository.StatisticsRepository;
import troublog.backend.domain.trouble.enums.TagType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public List<DailyCountResDto> getDailyPostCountByUser(Long userId) {
        LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = LocalDate.now();

        List<Object[]> result = statisticsRepository.findDailyPostCountByUser(userId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
        return result.stream()
                .map(row -> new DailyCountResDto(
                        ((java.sql.Date) row[0]).toLocalDate(),
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<StatsResDto> getTopTechStats(Long userId) {
        return statisticsRepository.findTopTagsByUser(userId, TagType.TECH_STACK)
                .stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<StatsResDto> getTopErrorStats(Long userId) {
        return statisticsRepository.findTopTagsByUser(userId, TagType.ERROR)
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<StatsResDto> getSummaryStats(Long userId) {
        return statisticsRepository.findSummaryTypesByUser(userId);
    }
}
