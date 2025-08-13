package troublog.backend.global.common.error.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AlertException;
import troublog.backend.global.common.error.exception.AuthException;
import troublog.backend.global.common.error.exception.BusinessException;
import troublog.backend.global.common.error.exception.PostException;
import troublog.backend.global.common.error.exception.AiTaskException;
import troublog.backend.global.common.error.exception.UserException;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.response.ErrorResponse;
import troublog.backend.global.common.util.LoggingUtil;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleException(Exception e, HttpServletRequest request) {
		LoggingUtil.logException("지정되지 않은 예외 발생", e, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleBusinessException(BusinessException e,
		HttpServletRequest request) {
		LoggingUtil.logException("BusinessException 발생", e, request);
		ErrorResponse response = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleAuthException(AuthException e,
		HttpServletRequest request) {
		LoggingUtil.logException("AuthException 발생", e, request);
		ErrorResponse response = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleUserException(UserException e,
		HttpServletRequest request) {
		LoggingUtil.logException("UserException 발생", e, request);
		ErrorResponse response = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(AlertException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleAlertException(AlertException e,
		HttpServletRequest request) {
		LoggingUtil.logException("AlertException 발생", e, request);
		ErrorResponse response = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(PostException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handlePostException(PostException e,
		HttpServletRequest request) {
		LoggingUtil.logException("PostException 발생", e, request);
		ErrorResponse response = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(AiTaskException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleSummaryException(AiTaskException e,
		HttpServletRequest request) {
		LoggingUtil.logException("SummaryException 발생", e, request);
		ErrorResponse response = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		HttpServletRequest request) {
		LoggingUtil.logValidationException(e, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT, request);
		response.addValidationErrors(e.getBindingResult());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(response));
	}

}