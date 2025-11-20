package troublog.backend.domain.auth.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PasswordEmailUUIDRes(
	@Schema(description = "랜덤문자열")
	UUID randomString
) {
}
