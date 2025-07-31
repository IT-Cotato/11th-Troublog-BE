package troublog.backend.domain.trouble.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostTagReqDto (
	@Schema(
		defaultValue = "FRONTEND",
		allowableValues = {
			"FRONTEND",
			"BACKEND",
			"DATABASE",
			"DEVOPS",
			"INFRA",
			"TOOL"
		}
	)
	String tagCategory
) {
}
