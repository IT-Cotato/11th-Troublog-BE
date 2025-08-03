package troublog.backend.domain.trouble.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.trouble.dto.response.common.ContentInfoDto;

@Builder
@Schema(description = "트러블로그 문서 응답 DTO")
public record PostResDto(
	@Schema(description = "게시글 ID", example = "1")
	Long id,

	@Schema(description = "게시글 제목", example = "Spring Boot 환경설정 트러블슈팅")
	String title,

	@Schema(description = "게시글 소개", example = "Spring Boot 프로젝트 설정 중 발생한 문제와 해결 과정")
	String introduction,

	@Schema(description = "좋아요 수", example = "15")
	int likeCount,

	@Schema(description = "댓글 수", example = "3")
	int commentCount,

	@Schema(description = "공개 여부", example = "true")
	boolean isVisible,

	@Schema(description = "요약 생성 여부", example = "false")
	boolean isSummaryCreated,

	@Schema(description = "삭제 여부", example = "false")
	boolean isDeleted,

	@Schema(description = "게시글 상태", example = "COMPLETED")
	String postStatus,

	@Schema(description = "별점", example = "1")
	String starRating,

	@Schema(description = "작성 시간", example = "2024-01-15T10:00:00")
	LocalDateTime createdAt,

	@Schema(description = "수정 시간", example = "2024-01-15T15:00:00")
	LocalDateTime updatedAt,

	@Schema(description = "삭제 시간", example = "2024-01-15T15:00:00")
	LocalDateTime deletedAt,

	@Schema(description = "작성자 정보")
	long userId,

	@Schema(description = "프로젝트 정보")
	long projectId,

	@Schema(description = "에러 태그 정보", example = "RunTime Error")
	String errorTag,

	@Schema(description = "게시글 태그 목록")
	List<String> postTags,

	@Schema(description = "게시글 내용 목록")
	List<ContentInfoDto> contents
) {

}
