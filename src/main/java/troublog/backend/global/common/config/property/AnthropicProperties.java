package troublog.backend.global.common.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@ConfigurationProperties("spring.ai.anthropic")
public record AnthropicProperties(
	@NotBlank
	String apiKey,
	@Valid
	@NotNull
	Chat chat
) {
	public record Chat(
		@Valid
		@NotNull
		Options options
	) {
	}

	public record Options(
		@NotBlank
		String model) {
	}
}