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

	public CustomAuthenticationToken(Object principal, Object credentials, Long userId, String envType, String nickname) {
		super(principal, credentials);
		this.userId = userId;
		this.envType = envType;
		this.nickname = nickname;
	}

	public CustomAuthenticationToken(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities, Long userId, String envType, String nickname) {
		super(principal, credentials, authorities);
		this.userId = userId;
		this.envType = envType;
		this.nickname = nickname;
	}

	public static CustomAuthenticationToken unauthenticated(String email, String password, Long userId, String clientEnvType, String nickname) {
		return new CustomAuthenticationToken(email, password, userId, clientEnvType, nickname);
	}

	public static CustomAuthenticationToken authenticated(String email, String password, Long userId, String clientEnvType, String nickname) {
		return new CustomAuthenticationToken(email, password, List.of(), userId, clientEnvType, nickname);
	}
}
