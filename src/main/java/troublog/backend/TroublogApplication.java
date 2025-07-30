package troublog.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfigurationPropertiesScan
@EnableConfigurationProperties
@SpringBootApplication
@EnableJpaAuditing
public class TroublogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TroublogApplication.class, args);
	}

}
