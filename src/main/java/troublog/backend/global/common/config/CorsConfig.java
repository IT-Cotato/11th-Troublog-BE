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

	/**
	 * Creates and configures a {@link CorsFilter} bean with specific CORS settings for the application.
	 *
	 * <p>
	 * Allows credentials and permits requests from {@code http://localhost:3000} and {@code http://localhost:8080}.
	 * All headers and HTTP methods are allowed. Exposes the {@code accessToken}, {@code Authorization},
	 * {@code refreshToken}, and {@code Set-Cookie} headers to clients. The configuration applies to all URL patterns.
	 * </p>
	 *
	 * @return a configured {@link CorsFilter} bean for handling CORS requests
	 */
	@Bean
	public CorsFilter corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin(CLIENT_LOCAL);
		config.addAllowedOrigin(SERVER_LOCAL);
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
