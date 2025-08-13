package troublog.backend.global.common.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "ai.prompts")
public record PromptProperties(
	@NotNull Resource system,
	@NotNull Resource resume,
	@NotNull Resource interview,
	@NotNull Resource blog,
	@NotNull Resource issueManagement
) {
}