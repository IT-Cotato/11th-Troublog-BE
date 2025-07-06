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
