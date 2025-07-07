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

	/**
	 * Authenticates a {@link CustomAuthenticationToken} and returns a new authenticated token instance.
	 *
	 * @param authentication the authentication request object, expected to be a {@link CustomAuthenticationToken}
	 * @return an authenticated {@link CustomAuthenticationToken} with user details set
	 * @throws AuthenticationException if authentication fails
	 */
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

	/**
	 * Determines whether this authentication provider supports the specified authentication type.
	 *
	 * @param authentication the class of the authentication object
	 * @return true if the authentication type is assignable from CustomAuthenticationToken; false otherwise
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return CustomAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
