package troublog.backend.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record UserDto() {

	@Builder
	@Schema(description = "팔로잉 / 팔로워 리스트 응답 DTO")
	public static record UserFollowsDto(

		@Schema(description = "유저 아이디")
		@JsonProperty("userId")
		Long userId,

		@Schema(description = "닉네임")
		@JsonProperty("nickname")
		String nickname,

		@Schema(description = "이메일")
		@JsonProperty("email")
		String email,

		@Schema(description = "프로필 이미지 주소")
		@JsonProperty("profileUrl")
		String profileUrl,

		@Schema(description = "사용자의 팔로우 여부")
		@JsonProperty("isFollowed")
		Boolean isFollowed

		) {}
}

