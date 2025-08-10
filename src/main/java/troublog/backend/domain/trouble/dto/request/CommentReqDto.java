package troublog.backend.domain.trouble.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "커뮤니티 댓글/대댓글 생성 DTO")
public record CommentReqDto(
	@NotNull
	@Schema(description = "포스트 id")
	Long postId,

	@NotNull
	@Schema(description = "댓글 내용")
	String contents

) {
}
