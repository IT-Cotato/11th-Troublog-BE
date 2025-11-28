package troublog.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PasswordEmailCheckReqDto(
	@NotBlank
	@Schema(description = "이메일")
	String email
) {
}
