package troublog.backend.global.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class LoggingUtil {

	/**
	 * 일반 예외를 로깅합니다. 스택트레이스 중 첫 번째 요소만 출력합니다.
	 *
	 * @param prefix  로그 메시지 앞에 붙일 식별자
	 * @param e       발생한 예외
	 * @param request 요청 정보
	 */
	public void logException(String prefix, Exception e, HttpServletRequest request) {
		String firstStack = Arrays.stream(e.getStackTrace())
			// .findFirst()
			.map(StackTraceElement::toString)
			// .orElse("스택 없음");
			.toString();

		log.error("{}: {}", prefix, e.getMessage());
		log.error("예외 발생 지점 [{} {}]", request.getMethod(), request.getRequestURI());
		log.error("StackTrace: {}", firstStack);
	}

	/**
	 * 유효성 검사 예외를 로깅합니다. 실패한 필드와 메시지를 모두 출력합니다.
	 *
	 * @param ex      MethodArgumentNotValidException
	 * @param request 요청 정보
	 */
	public void logValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
		String errorFields = ex.getBindingResult().getFieldErrors().stream()
			.map(LoggingUtil::formatFieldError)
			.collect(Collectors.joining(" "));

		log.error("MethodArgumentNotValidException 발생 : 유효성 검사 실패");
		log.error("예외 발생 지점 [{} {}]", request.getMethod(), request.getRequestURI());
		log.error("StackTrace: {}", errorFields);
	}

	private String formatFieldError(FieldError error) {
		return String.format("[field: %s, message: %s]", error.getField(), error.getDefaultMessage());
	}
}
