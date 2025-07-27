package troublog.backend.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자 프로필 수정 요청 DTO")
public record UserProfileUpdateReqDto(

	@Schema(description = "유저 아이디")
	@JsonProperty("userId")
	Long userId,

	@Schema(description = "닉네임")
	@JsonProperty("nickname")
	String nickname,

	@Schema(description = "분야")
	@JsonProperty("field")
	String field,

	@Schema(description = "한 줄 소개")
	@JsonProperty("bio")
	String bio,

	@Schema(description = "깃허브 주소")
	@JsonProperty("githubUrl")
	String githubUrl,

	@Schema(description = "프로필 이미지 주소")
	@JsonProperty("profileUrl")
	String profileUrl
) {
}
