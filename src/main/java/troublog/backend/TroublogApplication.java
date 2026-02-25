package troublog.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationPropertiesScan
@EnableConfigurationProperties
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class TroublogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TroublogApplication.class, args);
	}

}
