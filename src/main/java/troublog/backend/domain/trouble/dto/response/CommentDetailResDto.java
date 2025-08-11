package troublog.backend.domain.trouble.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "커뮤니티 댓글 상세 조회 응답 DTO")
public record CommentDetailResDto(

	@NotNull
	@Schema(description = "댓글 commentId")
	Long commentId,

	@NotNull
	@Schema(description = "포스트 commentId")
	Long postId,

	@NotNull
	@Schema(description = "유저 commentId")
	Long userId,

	@NotNull
	@Schema(description = "댓글 내용")
	String content,

	@NotNull
	@Schema(description = "댓글 생성 시간")
	LocalDateTime createdAt,

	@Schema(description = "대댓글 리스트")
	List<CommentResDto> commentList

) {

}

