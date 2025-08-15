package troublog.backend.domain.trouble.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "커뮤니티 댓글/대댓글 응답 DTO")
public record CommentResDto(

	@NotNull
	@Schema(description = "댓글 commentId")
	Long commentId,

	@NotNull
	@Schema(description = "포스트 commentId")
	Long postId,

	@NotNull
	@Schema(description = "유저 Id")
	Long userId,

	@NotNull
	@Schema(description = "유저 닉네임")
	String name,

	@Schema(description = "프로필 사진")
	String profileImg,

	@NotNull
	@Schema(description = "댓글 내용")
	String content,

	@NotNull
	@Schema(description = "댓글 생성 시간")
	LocalDateTime createdAt,

	@Schema(description = "상위 댓글 commentId")
	Long parentCommentId
) {

}
