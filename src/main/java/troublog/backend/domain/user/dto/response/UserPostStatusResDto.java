package troublog.backend.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserPostStatusResDto(

	@Schema(description = "유저 아이디")
	Long userId,

	@Schema(description = "작성 상태")
	String postStatus,

	@Schema(description = "게시글 ID 리스트")
	Long[] postIdList
) {
}
