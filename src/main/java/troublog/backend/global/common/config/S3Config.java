package troublog.backend.global.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import troublog.backend.global.common.config.property.AwsProperties;

@Configuration
@RequiredArgsConstructor
public class S3Config {

	private final AwsProperties awsProperties;

	@Bean
	public S3Client s3Client() {
		AwsBasicCredentials credentials = AwsBasicCredentials.create(
			awsProperties.credentials().accessKey(),
			awsProperties.credentials().secretKey()
		);

		return S3Client.builder()
			.region(Region.of(awsProperties.s3().region()))
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.build();
	}
}