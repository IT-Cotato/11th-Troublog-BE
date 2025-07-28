package troublog.backend.global.common.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cloud.aws")
public record AwsProperties(
	S3 s3,
	Region region,
	Stack stack,
	Credentials credentials
) {
	public record S3(String bucket) {
	}

	public record Region(String _static) {
	}

	public record Stack(Boolean auto) {
	}

	public record Credentials(String accessKey, String secretKey) {
	}
}