package troublog.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDto {

	@NotBlank
	@Schema(description = "이메일")
	@JsonProperty("email")
	private String email;

	@NotBlank
	@Schema(description = "비밀번호")
	@JsonProperty("password")
	private String password;

	@NotBlank
	@Schema(description = "닉네임")
	@JsonProperty("nickname")
	private String nickname;

	@NotBlank
	@Schema(description = "분야")
	@JsonProperty("field")
	private String field;

	@NotBlank
	@Schema(description = "한 줄 소개")
	@JsonProperty("bio")
	private String bio;

	@Schema(description = "깃허브 주소")
	@JsonProperty("githubUrl")
	private String githubUrl;
}
