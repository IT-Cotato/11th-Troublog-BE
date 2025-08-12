package troublog.backend.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PostCardUserInfoResDto(
	@Schema(description = "유저 ID")
	Long userId,

	@Schema(description = "닉네임")
	String nickname,

	@Schema(description = "유저 프로필 이미지")
	String profileImageUrl
) {
}