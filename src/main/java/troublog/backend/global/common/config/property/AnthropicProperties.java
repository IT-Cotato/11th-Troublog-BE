package troublog.backend.global.common.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("spring.ai.anthropic")
public record AnthropicProperties(
	@NotBlank
	String apiKey,
	Chat chat
) {
	public record Chat(
		Options options
	) {
	}

	public record Options(
		@NotBlank
		String model) {
	}
}