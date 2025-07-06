package troublog.backend.global.common.util;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.constant.EnvType;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AuthException;

@Component
@RequiredArgsConstructor
public class DataUtil {

	private final JwtProvider jwtProvider;

	@Value("${spring.profiles.active}")
	private String profilesActive;

	public static String getCookieValue(Cookie[] cookies, String name) {
		if(cookies == null || name == null) {
			return null;
		}
		for(Cookie cookie : cookies) {
			if(name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public static String getValueFromRequest(HttpServletRequest request, String header) {

		// 헤더가 없거나 공백이면 null 반환
		String value = request.getHeader(header);
		if (!StringUtils.hasText(value)) {
			return null;
		}

		if (AUTHORIZATION.equalsIgnoreCase(header)) {
			return value.startsWith("Bearer ") ? value.substring(7).trim() : null;
		}

		// 그 외 헤더는 공백만 Trim 해서 그대로 반환
		return value.trim();
	}

	public void checkEnvType(String clientEnvType) {

		EnvType serverEnvType = EnvType.valueOfEnvType(profilesActive);
		EnvType frontEnvType = EnvType.valueOfEnvType(clientEnvType);

		if(serverEnvType != null &&
			!serverEnvType.isLocal() &&
			!(frontEnvType.isLocal() && serverEnvType.isDev()) &&
			!EnvType.isEqualEnvType(serverEnvType, frontEnvType)) {

			throw new AuthException(ErrorCode.WRONG_ENVIRONMENT);
		}
	}

}
