package troublog.backend.global.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	private static final String CLIENT_LOCAL = "http://localhost:3000";
	private static final String SERVER_LOCAL = "http://localhost:8080";

	@Bean
	public CorsFilter corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of(CLIENT_LOCAL, SERVER_LOCAL));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setExposedHeaders(List.of(
			"accessToken",
			"Authorization",
			"refreshToken"
		));
		config.addExposedHeader("Set-Cookie");

		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
