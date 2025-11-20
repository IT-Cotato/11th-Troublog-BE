package troublog.backend.domain.auth.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PasswordAuthCodeCheckReq(
	@NotBlank
	@Schema(description = "인증코드")
	String authCode,
	@NotNull
	@Schema(description = "랜덤문자열")
	UUID randomString
) {
}
