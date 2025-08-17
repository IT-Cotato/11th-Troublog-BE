package troublog.backend.domain.trouble.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.user.dto.response.PostCardUserInfoResDto;

@Builder
public record PostCardResDto(
	@Schema(description = "사용자 정보")
	PostCardUserInfoResDto postCardUserInfoResDto,

	@Schema(description = "트러블슈팅 id")
	Long id,

	@Schema(description = "트러블슈팅 제목")
	String title,

	@Schema(description = "이미지 URL")
	String thumbnailUrl,

	@Schema(description = "날짜")
	String completedAt,

	@Schema(description = "에러 태그 정보", example = "RunTime Error")
	String errorTag,

	@Schema(description = "상위 3개 기술 태그")
	List<String> postTags,

	@Schema(description = "좋아요 수", example = "15")
	int likeCount,

	@Schema(description = "댓글 수", example = "3")
	int commentCount
) {

}
