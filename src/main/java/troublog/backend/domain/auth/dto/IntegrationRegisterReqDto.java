package troublog.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record IntegrationRegisterReqDto(
	@Schema(description = "이메일")
	String email,
	@Schema(description = "비밀번호")
	String password
) {
}
