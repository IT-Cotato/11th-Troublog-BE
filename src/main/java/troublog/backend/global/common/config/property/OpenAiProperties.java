package troublog.backend.global.common.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("spring.ai.openai")
public record OpenAiProperties(
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
		String model,
		int maxTokens
	) {
	}
}