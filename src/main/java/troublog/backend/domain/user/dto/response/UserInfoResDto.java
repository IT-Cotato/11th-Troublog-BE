package troublog.backend.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 조회 응답 DTO")
public record UserInfoResDto(
	@Schema(description = "유저 아이디")
	@JsonProperty("userId")
	Long userId,

	@Schema(description = "닉네임")
	@JsonProperty("nickname")
	String nickname,

	@Schema(description = "프로필 이미지 주소")
	@JsonProperty("profileUrl")
	String profileUrl,

	@Schema(description = "한 줄 소개")
	@JsonProperty("bio")
	String bio,

	@Schema(description = "팔로워 수")
	@JsonProperty("followerNum")
	Long followerNum,

	@Schema(description = "팔로잉 수")
	@JsonProperty("followingNum")
	Long followingNum,

	@Schema(description = "사용자의 팔로우 여부")
	@JsonProperty("isFollowed")
	Boolean isFollowed
) {
}
