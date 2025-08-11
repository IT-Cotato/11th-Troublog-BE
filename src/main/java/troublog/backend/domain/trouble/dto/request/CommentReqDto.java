package troublog.backend.domain.trouble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "커뮤니티 댓글/대댓글 생성 DTO")
public record CommentReqDto(

	@NotBlank
	@Schema(description = "댓글 내용")
	String contents

) {
}
