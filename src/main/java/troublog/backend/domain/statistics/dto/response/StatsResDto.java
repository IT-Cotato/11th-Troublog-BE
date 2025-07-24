package troublog.backend.domain.statistics.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "통계 응답 DTO")
public record StatsResDto (

        @Schema(description = "분석 결과 이름")
        String name,

        @Schema(description = "개수")
        long count
){
}
