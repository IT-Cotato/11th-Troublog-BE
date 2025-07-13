package troublog.backend.global.common.util;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataUtil {

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

}
