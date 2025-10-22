package troublog.backend.domain.auth.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OAuth2RegisterReqDto(

	@NotNull
	@Schema(description = "사용자 아이디")
	@JsonProperty("userId")
	Long userId,

	@NotBlank
	@Schema(description = "카카오 닉네임")
	@JsonProperty("kakaoNickname")
	String kakaoNickname,

	@NotBlank
	@Schema(description = "사용자 입력 닉네임")
	@JsonProperty("nickname")
	String nickname,

	@NotBlank
	@Schema(description = "분야")
	@JsonProperty("field")
	String field,

	@NotBlank
	@Schema(description = "한 줄 소개")
	@JsonProperty("bio")
	String bio,

	@Schema(description = "깃허브 주소")
	@JsonProperty("githubUrl")
	String githubUrl,

	@NotNull
	@NotEmpty
	@Schema(description = "이용약관 동의 내역")
	@JsonProperty("termsAgreements")
	Map<Long, Boolean> termsAgreements
) {
}
