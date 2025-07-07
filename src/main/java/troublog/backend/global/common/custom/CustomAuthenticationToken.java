package troublog.backend.global.common.custom;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private final Long userId;
	private final String envType;
	private final String nickname;

	/**
	 * Constructs an unauthenticated CustomAuthenticationToken with the specified principal, credentials, user ID, environment type, and nickname.
	 *
	 * The token is created without any granted authorities, representing an unauthenticated authentication state.
	 */
	public CustomAuthenticationToken(Object principal, Object credentials, Long userId, String envType, String nickname) {
		super(principal, credentials);
		this.userId = userId;
		this.envType = envType;
		this.nickname = nickname;
	}

	/**
	 * Constructs an authenticated CustomAuthenticationToken with the specified principal, credentials, authorities, user ID, environment type, and nickname.
	 *
	 * @param principal the identity of the principal being authenticated
	 * @param credentials the credentials that prove the identity of the principal
	 * @param authorities the collection of granted authorities for the principal
	 * @param userId the unique identifier of the user
	 * @param envType the environment type associated with the authentication
	 * @param nickname the nickname of the user
	 */
	public CustomAuthenticationToken(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities, Long userId, String envType, String nickname) {
		super(principal, credentials, authorities);
		this.userId = userId;
		this.envType = envType;
		this.nickname = nickname;
	}

	/**
	 * Creates an unauthenticated CustomAuthenticationToken with the provided user information.
	 *
	 * @param email the user's email address to be used as the principal
	 * @param password the user's password to be used as credentials
	 * @param userId the unique identifier of the user
	 * @param clientEnvType the environment type associated with the client
	 * @param nickname the user's nickname
	 * @return a CustomAuthenticationToken instance without granted authorities, representing an unauthenticated state
	 */
	public static CustomAuthenticationToken unauthenticated(String email, String password, Long userId, String clientEnvType, String nickname) {
		return new CustomAuthenticationToken(email, password, userId, clientEnvType, nickname);
	}

	/**
	 * Creates an authenticated {@code CustomAuthenticationToken} with the specified user details and no authorities.
	 *
	 * @param email the user's email address to be used as the principal
	 * @param password the user's password to be used as the credentials
	 * @param userId the unique identifier of the user
	 * @param clientEnvType the environment type associated with the client
	 * @param nickname the user's nickname
	 * @return an authenticated {@code CustomAuthenticationToken} instance with an empty list of authorities
	 */
	public static CustomAuthenticationToken authenticated(String email, String password, Long userId, String clientEnvType, String nickname) {
		return new CustomAuthenticationToken(email, password, List.of(), userId, clientEnvType, nickname);
	}
}
