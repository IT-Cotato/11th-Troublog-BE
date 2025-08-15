package troublog.backend.domain.trouble.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.trouble.dto.response.common.SummaryContentInfoDto;

@Builder
public record PostSummaryResDto(
	@Schema(description = "게시글 요약 ID", example = "101")
	Long summaryId,

	@Schema(description = "게시글 제목", example = "Spring Boot 환경설정 트러블슈팅")
	String title,

	@Schema(description = "작성자 ID", example = "42")
	Long userId,

	@Schema(description = "프로젝트 ID", example = "7")
	Long projectId,

	@Schema(description = "요약 타입", example = "SHORT")
	String summaryType,

	@Schema(description = "에러 태그", example = "RunTime Error")
	String errorTag,

	@Schema(description = "기술 스택 태그 목록")
	List<String> postTags,

	@Schema(description = "요약 내용 목록")
	List<SummaryContentInfoDto> summaryContents,

	@Schema(description = "요약 생성일", example = "2024-06-01")
	@DateTimeFormat(pattern = "yyyy.mm.dd")
	LocalDateTime summaryCreatedAt
) {
}