package troublog.backend.global.common.util;

import java.util.stream.Collectors;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class LoggingUtil {

	/**
	 * 일반 예외를 로깅합니다. Logback의 exception/stacktrace 필드를 활용합니다.
	 *
	 * @param prefix  로그 메시지 앞에 붙일 식별자
	 * @param ex      발생한 예외
	 * @param request 요청 정보
	 */
	public void logException(final String prefix, final Exception ex, final HttpServletRequest request) {
		log.error("{}: {} | 예외 발생 지점 [{} {}]",
			prefix,
			ex.getMessage(),
			request.getMethod(),
			request.getRequestURI(),
			ex);
	}

	/**
	 * 유효성 검사 예외를 로깅합니다. 실패한 필드와 메시지를 출력합니다.
	 *
	 * @param ex      MethodArgumentNotValidException
	 * @param request 요청 정보
	 */
	public void logValidationException(final MethodArgumentNotValidException ex, final HttpServletRequest request) {
		String errorFields = ex.getBindingResult().getFieldErrors().stream()
			.map(LoggingUtil::formatFieldError)
			.collect(Collectors.joining(", "));

		log.error("유효성 검사 실패 | 예외 발생 지점 [{} {}] | 실패 필드: {}",
			request.getMethod(),
			request.getRequestURI(),
			errorFields,
			ex);
	}

	private String formatFieldError(final FieldError error) {
		return String.format("[field: %s, message: %s]", error.getField(), error.getDefaultMessage());
	}
}
