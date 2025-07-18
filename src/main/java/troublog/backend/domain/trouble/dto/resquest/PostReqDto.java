package troublog.backend.domain.trouble.dto.resquest;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "트러블로그 게시글 생성 요청 DTO")
public record PostReqDto(
	@Schema(
		description = "게시글 제목",
		example = "Spring Boot 환경설정 트러블슈팅"
	)
	@NotBlank(message = "제목은 빈칸일 수 없습니다.")
	String title,

	@Schema(
		description = "에러 태그",
		example = "ConfigurationError",
		allowableValues = {
			"Build/Compile Error",
			"Runtime Error",
			"Dependency/Version Error",
			"Network/API Error",
			"Authentication/Authorization Error",
			"Database Error",
			"UI/Rendering Error",
			"Configuration Error",
			"Timeout/Error Handling",
			"Third‑party Library Error"
		}
	)
	String errorTag,

	@Schema(
		description = "게시글 태그 목록",
		example = "[\"Spring Boot\", \"Configuration\", \"Error\"]"
	)
	List<String> postTags,

	@Schema(description = "게시글 내용 목록")
	List<ContentDto> contentDtoList,

	@Schema(
		description = "게시글 소개",
		example = "Spring Boot 프로젝트 설정 중 발생한 문제와 해결 과정"
	)
	String introduction,

	@Schema(
		description = "게시글 공개 여부",
		example = "true"
	)
	boolean isVisible,

	@Schema(
		description = "요약 생성 여부",
		example = "false"
	)
	boolean isSummaryCreated,

	@Schema(
		description = "완료 일시",
		example = "2024-01-15T10:30:00"
	)
	LocalDateTime completedAt,

	@Schema(
		description = "작성 상태",
		example = "COMPLETED",
		allowableValues = {"WRITING", "COMPLETED", "SUMMARIZED"}
	)
	String postStatus,

	@Schema(
		description = "중요도",
		example = "5",
		allowableValues = {"1", "2", "3", "4", "5"}
	)
	String starRating,

	@Schema(
		description = "프로젝트 ID",
		example = "1"
	)
	int projectId
) {
}