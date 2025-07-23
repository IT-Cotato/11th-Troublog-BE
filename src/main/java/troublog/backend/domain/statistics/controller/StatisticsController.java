package troublog.backend.domain.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import troublog.backend.domain.statistics.dto.response.DailyCountResDto;
import troublog.backend.domain.statistics.dto.response.StatsResDto;
import troublog.backend.domain.statistics.service.StatisticsService;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@Tag(name = "통계 시각화", description = "마이페이지의 사용자 통계")
public class StatisticsController {

    private final StatisticsService statisticsService;

    // 트러블로그 활동 통계
    @GetMapping("/daily")
    @Operation(summary = "일일 트러블로그 활동 통계")
    public ResponseEntity<BaseResponse<List<DailyCountResDto>>> getUserDailyStats(
            @AuthenticationPrincipal CustomAuthenticationToken auth) {
        Long userId = auth.getUserId();
        List<DailyCountResDto> dailyList = statisticsService.getDailyPostStats(userId);
        return ResponseUtils.created(dailyList);
    }

    // 태그별 통계(상위 5개)
    @GetMapping("/tags")
    @Operation(summary = "태그별 통계")
    public ResponseEntity<BaseResponse<List<StatsResDto>>> getTopTags(
            @AuthenticationPrincipal CustomAuthenticationToken auth){
        Long userId = auth.getUserId();
        List<StatsResDto> tags = statisticsService.getTopTagStats(userId);
        return ResponseUtils.created(tags);
    }

    // 에러 종류별 통계(상위 3개)
    @GetMapping("/errors")
    @Operation(summary = "에러 종류별 통계")
    public ResponseEntity<BaseResponse<List<StatsResDto>>> getTopErrors(
            @AuthenticationPrincipal CustomAuthenticationToken auth){
        Long userId = auth.getUserId();
        List<StatsResDto> errors = statisticsService.getTopErrorStats(userId);
        return ResponseUtils.created(errors);
    }

    // 요약본 종류 통계
    @GetMapping("/summary")
    @Operation(summary = "요약본 종류 통계")
    public ResponseEntity<BaseResponse<List<StatsResDto>>> getTopSummary(
            @AuthenticationPrincipal CustomAuthenticationToken auth){
        Long userId = auth.getUserId();
        List<StatsResDto> summaries = statisticsService.getSummaryStats(userId);
        return ResponseUtils.created(summaries);
    }

}
