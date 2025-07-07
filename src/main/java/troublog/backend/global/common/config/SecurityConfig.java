package troublog.backend.global.common.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.filter.ExceptionHandlerFilter;
import troublog.backend.global.common.filter.JwtAuthenticationFilter;
import troublog.backend.global.common.util.JwtAuthenticationProvider;
import troublog.backend.global.common.util.JwtProvider;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ExceptionHandlerFilter exceptionHandlerFilter;
	private final CorsFilter corsFilter;


	/**
	 * Configures the application's security filter chain with stateless JWT-based authentication.
	 *
	 * Disables CSRF protection, HTTP Basic authentication, form login, and logout. Sets session management to stateless mode and adds a CORS filter. Permits unrestricted access to actuator endpoints, authentication endpoints, Swagger UI, API documentation, error pages, and the root path. All other requests require authentication. Integrates custom JWT authentication and exception handler filters into the filter chain.
	 *
	 * @param http the HttpSecurity to configure
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilter(corsFilter);
		http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(EndpointRequest.toAnyEndpoint())
				.permitAll()
				.requestMatchers("/auth/**", "/swagger-ui/**", "v3/api-docs/**", "/error", "/")
				.permitAll()
				.anyRequest()
				.authenticated()
			);
		http
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

		return http.build();
	}
	/**
	 * Configures and returns an {@link AuthenticationManager} that uses the custom JWT authentication provider.
	 *
	 * @param http the {@link HttpSecurity} context used to retrieve and configure the authentication manager
	 * @return the configured {@link AuthenticationManager} instance
	 * @throws Exception if an error occurs during authentication manager setup
	 */
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.authenticationProvider(jwtAuthenticationProvider);
		return builder.build();
	}
	/**
	 * Creates a password encoder bean that uses the BCrypt hashing algorithm.
	 *
	 * @return a PasswordEncoder instance for securely hashing passwords
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}