package troublog.backend.domain.auth.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterReqDto(

	@NotBlank
	@Schema(description = "이메일")
	@JsonProperty("email")
	String email,

	@NotBlank
	@Schema(description = "비밀번호")
	@JsonProperty("password")
	String password,

	@NotBlank
	@Schema(description = "닉네임")
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
	@JsonProperty("agreementReqDto")
	Map<Long, Boolean> termsAgreements
) {}