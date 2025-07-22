package troublog.backend.domain.trouble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "콘텐츠 정보")
public record ContentInfoDto(
	@Schema(description = "콘텐츠 ID", example = "1")
	Long id,

	@Schema(description = "소제목", example = "문제 발생")
	String subTitle,

	@Schema(description = "본문 내용", example = "애플리케이션 실행 중 다음과 같은 오류가 발생했습니다.")
	String body,

	@Schema(description = "순서", example = "1")
	int sequence,

	@Schema(description = "작성자 유형", example = "USER_WRITTEN")
	String authorType,

	@Schema(description = "요약 유형", example = "BLOG")
	String summaryType
) {
}
