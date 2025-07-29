package troublog.backend.global.common.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties("cloud.aws")
public record AwsProperties(
	S3 s3,
	Region region,
	Stack stack,
	Credentials credentials
) {
	public record S3(String bucket) {
	}

	public record Region(@Name("static") String staticRegion) {
	}

	public record Stack(Boolean auto) {
	}

	public record Credentials(String accessKey, String secretKey) {
	}
}