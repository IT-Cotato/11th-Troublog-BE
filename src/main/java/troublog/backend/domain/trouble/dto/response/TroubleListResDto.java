package troublog.backend.domain.trouble.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "트러블슈팅 목록 응답 DTO")
public record TroubleListResDto(

	@Schema(description = "트러블슈팅 id")
	Long id,

	@Schema(description = "요약본 id")
	Long postSummaryId,

	@Schema(description = "요약 타입")
	String summaryType,

	@Schema(description = "프로젝트 id")
	Long projectId,

	@Schema(description = "트러블슈팅 제목")
	String title,

	@Schema(description = "이미지 URL")
	String imageUrl,

	@Schema(description = "공개/비공개 여부")
	Boolean isVisible,

	@Schema(description = "날짜")
	LocalDateTime date,

	@Schema(description = "중요도")
	Integer starRating,

	@Schema(description = "에러 정보")
	String error,

	@Schema(description = "상위 3개 기술 태그")
	List<String> techs,

	@Schema(description = "작성 상태")
	String status,

	@Schema(description = "요약본 리스트")
	List<PostSummaryResDto> summaries

) {
}

