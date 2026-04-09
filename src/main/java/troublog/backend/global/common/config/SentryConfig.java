package troublog.backend.global.common.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.sentry.protocol.User;
import io.sentry.spring.jakarta.SentryUserProvider;

@Configuration
public class SentryConfig {

	@Bean
	public SentryUserProvider sentryUserProvider() {
		return () -> {
			String userId = MDC.get("userId");
			if (userId == null) {
				return null;
			}
			User user = new User();
			user.setId(userId);
			return user;
		};
	}
}
