package troublog.backend.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "팔로잉 / 팔로워 리스트 응답 DTO")
public record UserFollowsResDto(
	@Schema(description = "유저 아이디")
	Long userId,

	@Schema(description = "닉네임")
	String nickname,

	@Schema(description = "이메일")
	String email,

	@Schema(description = "프로필 이미지 주소")
	String profileUrl,

	@Schema(description = "사용자의 팔로우 여부")
	Boolean isFollowed
) {
}
