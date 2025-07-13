package troublog.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginReqDto(

	@NotBlank
	@Schema(description = "이메일")
	@JsonProperty("email")
	String email,

	@NotBlank
	@Schema(description = "비밀번호")
	@JsonProperty("password")
	String password

) {}