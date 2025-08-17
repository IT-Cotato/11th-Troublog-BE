package troublog.backend.domain.trouble.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.trouble.dto.response.common.ContentInfoDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;


@Builder
public record PostDetailsResDto(
	@Schema(description = "사용자 정보")
	UserInfoResDto userInfoResDto,

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

	@Schema(description = "좋아요 여부", example = "true")
	boolean liked,

	@Schema(description = "작성 완료 시간", example = "2024-01-15")
	String completedAt,

	@Schema(description = "에러 태그 정보", example = "RunTime Error")
	String errorTag,

	@Schema(description = "게시글 태그 목록")
	List<String> postTags,

	@Schema(description = "게시글 내용 목록")
	List<ContentInfoDto> contents
) {

}
