package troublog.backend.domain.auth.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterReqDto(

	@NotBlank
	@Schema(description = "이메일")
	String email,

	@NotBlank
	@Schema(description = "비밀번호")
	String password,

	@NotBlank
	@Schema(description = "닉네임")
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