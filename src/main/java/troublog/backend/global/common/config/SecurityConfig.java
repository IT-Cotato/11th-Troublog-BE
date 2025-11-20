package troublog.backend.global.common.config;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import troublog.backend.domain.auth.handler.OAuth2LoginSuccessHandler;
import troublog.backend.global.common.filter.ExceptionHandlerFilter;
import troublog.backend.global.common.filter.JwtAuthenticationFilter;
import troublog.backend.global.common.util.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {

	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ExceptionHandlerFilter exceptionHandlerFilter;
	private final CorsConfig corsFilter;

	private static final String[] WHITELIST = {
		"/auth/register",
		"/auth/login",
		"/auth/refresh",
		"/auth/email-check",
		"/auth/oauth-register",
		"/auth/find-password",
		"/auth/check-code",
		"/auth/change-password",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/error",
		"/image",
		"/grafana",
		"/grafana/**",
		"/terms/latest"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(SecurityConfig::createSessionPolicy);

		http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(EndpointRequest.toAnyEndpoint())
				.permitAll()
				.requestMatchers(WHITELIST)
				.permitAll()
				.anyRequest()
				.authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.successHandler(oAuth2LoginSuccessHandler));

		http
			.addFilterBefore(exceptionHandlerFilter, CorsFilter.class)
			.addFilterBefore(corsFilter.corsFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.authenticationProvider(jwtAuthenticationProvider);
		return builder.build();
	}

	private static void createSessionPolicy(SessionManagementConfigurer<HttpSecurity> session) {
		session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}