package troublog.backend.domain.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import troublog.backend.domain.statistics.dto.response.DailyCountResDto;
import troublog.backend.domain.statistics.dto.response.StatsResDto;
import troublog.backend.domain.statistics.service.StatisticsService;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@Tag(name = "마이페이지", description = "사용자 통계")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/daily")
    @Operation(summary = "일일 트러블로그 활동 통계 API", description = "1회 이상일 때만 반환한다.")
    public ResponseEntity<BaseResponse<List<DailyCountResDto>>> getUserDailyStats(
            @Authentication CustomAuthenticationToken auth) {
        Long userId = auth.getUserId();
        List<DailyCountResDto> dailyList = statisticsService.getDailyPostCountByUser(userId);
        return ResponseUtils.ok(dailyList);
    }

    @GetMapping("/tags")
    @Operation(summary = "기술 태그별 통계 API", description = "가장 많이 사용한 기술 태그 상위 5개를 반환한다.")
    public ResponseEntity<BaseResponse<List<StatsResDto>>> getTopTech(
            @Authentication CustomAuthenticationToken auth){
        Long userId = auth.getUserId();
        List<StatsResDto> tags = statisticsService.getTopTechStats(userId);
        return ResponseUtils.ok(tags);
    }

    @GetMapping("/errors")
    @Operation(summary = "에러 태그별 통계 API", description = "가장 많이 사용한 에러 태그 상위 3개를 반환한다.")
    public ResponseEntity<BaseResponse<List<StatsResDto>>> getTopError(
            @Authentication CustomAuthenticationToken auth){
        Long userId = auth.getUserId();
        List<StatsResDto> errors = statisticsService.getTopErrorStats(userId);
        return ResponseUtils.ok(errors);
    }

    @GetMapping("/summary")
    @Operation(summary = "요약본 종류 통계 API", description = "요약본의 종류와 해당 개수를 반환한다.")
    public ResponseEntity<BaseResponse<List<StatsResDto>>> getTopSummary(
            @Authentication CustomAuthenticationToken auth){
        Long userId = auth.getUserId();
        List<StatsResDto> summaries = statisticsService.getSummaryStats(userId);
        return ResponseUtils.ok(summaries);
    }

}
