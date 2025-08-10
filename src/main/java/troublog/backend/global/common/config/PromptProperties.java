package troublog.backend.global.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "ai.prompts")
public record PromptProperties(
	Resource system,
	Resource resume,
	Resource interview,
	Resource blog,
	Resource issueManagement
) {
}