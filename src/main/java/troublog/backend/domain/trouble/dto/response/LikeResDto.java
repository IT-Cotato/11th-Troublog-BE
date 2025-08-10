package troublog.backend.domain.trouble.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "포스트 좋아요 응답 DTO")
public record LikeResDto(
	@Schema(description = "게시글 ID")
	Long postId,

	@Schema(description = "유저 commentId")
	Long userId,

	@Schema(description = "좋아요 수")
	int likeCount

) {
}
