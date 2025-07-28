package troublog.backend.global.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.config.property.AwsProperties;

@Configuration
@RequiredArgsConstructor
public class S3Config {

	private final AwsProperties awsProperties;

	@Bean
	public AmazonS3Client amazonS3Client() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(
			awsProperties.credentials().accessKey(),
			awsProperties.credentials().secretKey()
		);

		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
			.withRegion(awsProperties.region()._static())
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.build();
	}
}