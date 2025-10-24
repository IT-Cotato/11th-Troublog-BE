package troublog.backend.global.common.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import troublog.backend.global.common.constant.Domain;

@Configuration
public class CorsConfig {
	private static final List<String> ALLOWED_ORIGINS = List.of(
		"http://localhost:3000",
		"http://localhost:8080",
		Domain.LOCAL.getUrl(),
		"https://troublog.shop",
		Domain.DEV.getUrl()
	);

	private static final List<String> ALLOWED_HEADERS = List.of(
		"Authorization",
		"Content-Type",
		"Accept",
		"Origin",
		"Access-Control-Request-Method",
		"Access-Control-Request-Headers",
		"X-Requested-With",
		"accessToken",
		"refreshToken",
		"EnvType"
	);

	private static final List<String> ALLOWED_METHODS = List.of(
		"GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
	);

	private static final List<String> EXPOSED_HEADERS = List.of(
		"accessToken",
		"Authorization",
		"refreshToken",
		"Set-Cookie",
		"Access-Control-Allow-Origin",
		"Access-Control-Allow-Credentials"
	);

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(ALLOWED_ORIGINS);
		config.setAllowedHeaders(ALLOWED_HEADERS);
		config.setAllowedMethods(ALLOWED_METHODS);
		config.setExposedHeaders(EXPOSED_HEADERS);
		config.setMaxAge(3600L);

		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
