package troublog.backend.global.common.filter;

import java.io.IOException;
import java.util.Arrays;
import static org.springframework.http.HttpHeaders.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.constant.EnvType;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AuthException;
import troublog.backend.global.common.util.DataUtil;
import troublog.backend.global.common.util.JwtProvider;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final DataUtil dataUtil;
	private final String ENVTYPE = "EnvType";

	@Value("${spring.profiles.active}")
	private String profilesActive;

	/**
	 * Processes incoming HTTP requests to authenticate users based on a JWT access token.
	 *
	 * Extracts the JWT token and environment type from request headers, validates the token, checks the environment type,
	 * and sets the authentication in the security context if valid. Continues the filter chain regardless of authentication outcome.
	 *
	 * @param request  the incoming HTTP request
	 * @param response the HTTP response
	 * @param filterChain the filter chain to proceed with
	 * @throws ServletException if an error occurs during filtering
	 * @throws IOException if an I/O error occurs during filtering
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException, IOException {

		String accessToken = DataUtil.getValueFromRequest(request, AUTHORIZATION);
		String clientEnvType = DataUtil.getValueFromRequest(request, ENVTYPE);

		if (accessToken != null && jwtProvider.validateToken(accessToken) && jwtProvider.isNotExpired(accessToken)) {

			dataUtil.checkEnvType(clientEnvType);

			Authentication authentication = jwtProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Determines whether the filter should be skipped for the given request based on its URI.
	 *
	 * The filter is not applied to requests whose paths match authentication endpoints, Swagger UI, or API documentation routes.
	 *
	 * @param request the current HTTP request
	 * @return {@code true} if the request should bypass the filter; {@code false} otherwise
	 * @throws ServletException if an error occurs during request processing
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String[] excludePath = {"/auth/**", "/swagger-ui/**", "/v3/api-docs/**"};
		String path = request.getRequestURI();
		return Arrays.stream(excludePath)
			.anyMatch(ep -> ep.endsWith("/**")
				? path.startsWith(ep.replace("/**", ""))
				: path.equals(ep));
	}
}
