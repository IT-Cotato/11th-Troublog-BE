package troublog.backend.global.common.util;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.custom.CustomAuthenticationToken;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;

		return CustomAuthenticationToken.authenticated(
			token.getName(),
			null,
			token.getUserId(),
			token.getEnvType(),
			token.getNickname()
		);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
