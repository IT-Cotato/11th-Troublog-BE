package troublog.backend.domain.auth.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OAuth2RegisterReqDto(

	@NotNull
	@Schema(description = "사용자 아이디")
	Long userId,

	@NotBlank
	@Schema(description = "카카오 닉네임")
	String kakaoNickname,

	@NotBlank
	@Schema(description = "사용자 입력 닉네임")
	String nickname,

	@NotBlank
	@Schema(description = "분야")
	String field,

	@NotBlank
	@Schema(description = "한 줄 소개")
	String bio,

	@Schema(description = "깃허브 주소")
	String githubUrl,

	@NotNull
	@NotEmpty
	@Schema(description = "이용약관 동의 내역")
	Map<Long, Boolean> termsAgreements
) {
}
