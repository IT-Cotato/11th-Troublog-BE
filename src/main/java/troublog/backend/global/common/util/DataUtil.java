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

	/**
	 * Retrieves the value of a cookie with the specified name from an array of cookies.
	 *
	 * @param cookies the array of cookies to search
	 * @param name the name of the cookie to find
	 * @return the value of the matching cookie, or {@code null} if not found or if input is {@code null}
	 */
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

	/**
	 * Retrieves the value of a specified HTTP header from the request, with special handling for the Authorization header.
	 *
	 * If the header is "Authorization" (case-insensitive) and its value starts with "Bearer ", returns the token part after "Bearer " trimmed of whitespace. For other headers, returns the trimmed header value. Returns {@code null} if the header is missing, empty, or if the Authorization header does not contain a Bearer token.
	 *
	 * @param request the HTTP servlet request
	 * @param header the name of the header to retrieve
	 * @return the processed header value, or {@code null} if not present or invalid
	 */
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

	/**
	 * Validates that the client-provided environment type matches the server's active environment profile.
	 *
	 * Throws an {@link AuthException} with {@code ErrorCode.WRONG_ENVIRONMENT} if the environments are incompatible,
	 * except when the server is in development and the client is in local mode.
	 *
	 * @param clientEnvType the environment type provided by the client
	 */
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
