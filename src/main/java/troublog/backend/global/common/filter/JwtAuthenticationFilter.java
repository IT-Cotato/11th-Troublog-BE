package troublog.backend.global.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import troublog.backend.global.common.util.DataUtil;
import troublog.backend.global.common.util.JwtProvider;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private static final String ENV_TYPE = "EnvType";

	private static final String[] EXCLUDE_PATHS = {
		"/auth/register",
		"/auth/login",
		"/auth/refresh",
		"/auth/email-check",
		"/auth/oauth-register",
		"/swagger-ui/**",
		"/v3/api-docs/**"
	};

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException, IOException {

		String accessToken = DataUtil.getValueFromRequest(request, AUTHORIZATION);
		String clientEnvType = DataUtil.getValueFromRequest(request, ENV_TYPE);

		if (accessToken != null && jwtProvider.validateToken(accessToken) && jwtProvider.isNotExpired(accessToken)) {

			jwtProvider.checkEnvType(clientEnvType);

			Authentication authentication = jwtProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return Arrays.stream(EXCLUDE_PATHS)
			.anyMatch(ep -> ep.endsWith("/**")
				? path.startsWith(ep.replace("/**", ""))
				: path.equals(ep));
	}
}
