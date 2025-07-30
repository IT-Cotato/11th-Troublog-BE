package troublog.backend.global.common.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.aws")
public record AwsProperties(
	S3 s3,
	Credentials credentials
) {
	public record S3(String region, String bucket) {
	}

	public record Credentials(String accessKey, String secretKey) {
	}
}