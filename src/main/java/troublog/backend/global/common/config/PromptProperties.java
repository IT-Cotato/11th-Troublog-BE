package troublog.backend.global.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "ai.prompts")
public record PromptProperties(
	@Valid
	@NotNull
	Resource system,

	@Valid
	@NotNull
	Resource resume,

	@Valid
	@NotNull
	Resource interview,

	@Valid
	@NotNull
	Resource memoirs,

	@Valid
	@NotNull
	Resource issueManagement
) {
}