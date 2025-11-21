package troublog.backend.global.common.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@ConfigurationProperties("spring.cloud.aws")
public record AwsProperties(
	@Valid
	@NotNull
	S3 s3,
	@Valid
	@NotNull
	Credentials credentials
) {
	public record S3(
		@NotBlank
		String region) {
	}

	public record Credentials(
		@NotBlank
		String accessKey,
		@NotBlank
		String secretKey) {
	}
}