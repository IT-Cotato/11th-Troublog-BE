package troublog.backend.global.common.error.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.BusinessException;
import troublog.backend.global.common.response.ApiResponse;
import troublog.backend.global.common.response.ErrorResponse;
import troublog.backend.global.common.util.LoggingUtil;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleException(Exception e, HttpServletRequest request) {
		LoggingUtil.logException("지정되지 않은 예외 발생", e, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(response));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(BusinessException e,
		HttpServletRequest request) {
		LoggingUtil.logException("BusinessException 발생", e, request);
		ErrorResponse response = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(response));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		HttpServletRequest request) {
		LoggingUtil.logValidationException(e, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT, request);
		response.addValidationErrors(e.getBindingResult());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(response));
	}

}