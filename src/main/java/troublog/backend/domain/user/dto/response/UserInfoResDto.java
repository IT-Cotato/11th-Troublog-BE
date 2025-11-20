package troublog.backend.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 조회 응답 DTO")
public record UserInfoResDto(
	@Schema(description = "유저 아이디")
	Long userId,

	@Schema(description = "닉네임")
	String nickname,

	@Schema(description = "프로필 이미지 주소")
	String profileUrl,

	@Schema(description = "한 줄 소개")
	String bio,

	@Schema(description = "팔로워 수")
	Long followerNum,

	@Schema(description = "팔로잉 수")
	Long followingNum,

	@Schema(description = "사용자의 팔로우 여부")
	Boolean isFollowed
) {
}
