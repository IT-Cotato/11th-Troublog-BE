package troublog.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PasswordChangeReq(
	@NotBlank
	@Schema(description = "이메일")
	String email,
	@NotBlank
	@Schema(description = "비밀번호")
	String password
) {
}
