package troublog.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginReqDto(

	@NotBlank
	@Schema(description = "이메일")
	String email,

	@NotBlank
	@Schema(description = "비밀번호")
	String password

) {
}