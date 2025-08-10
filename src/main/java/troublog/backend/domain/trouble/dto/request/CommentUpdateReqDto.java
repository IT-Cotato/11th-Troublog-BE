package troublog.backend.domain.trouble.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "커뮤니티 댓글/대댓글 수정 DTO")
public record CommentUpdateReqDto(

	@NotNull
	@Schema(description = "수정할 댓글 id")
	Long id,

	@NotNull
	@Schema(description = "댓글 내용")
	String contents,

	@Schema(description = "상위 댓글 id")
	Long parentCommentId

) {
}
