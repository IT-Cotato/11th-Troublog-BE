package troublog.backend.global.common.error.exception.handler;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AiTaskException;
import troublog.backend.global.common.error.exception.AlertException;
import troublog.backend.global.common.error.exception.AuthException;
import troublog.backend.global.common.error.exception.BusinessException;
import troublog.backend.global.common.error.exception.ImageException;
import troublog.backend.global.common.error.exception.PostException;
import troublog.backend.global.common.error.exception.ProjectException;
import troublog.backend.global.common.error.exception.TermsException;
import troublog.backend.global.common.error.exception.UserException;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.response.ErrorResponse;
import troublog.backend.global.common.util.LoggingUtil;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleException(Exception ex, HttpServletRequest request) {
		LoggingUtil.logException("지정되지 않은 예외 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleBusinessException(
		BusinessException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("BusinessException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleConstraintViolation(
		ConstraintViolationException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("ConstraintViolationException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("HttpMessageNotReadableException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("MissingServletRequestParameterException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleMethodArgumentTypeMismatch(
		MethodArgumentTypeMismatchException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("MethodArgumentTypeMismatchException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT, request);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("HttpRequestMethodNotSupportedException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED, request);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleHttpMediaTypeNotSupported(
		HttpMediaTypeNotSupportedException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("HttpMediaTypeNotSupportedException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.UNSUPPORTED_MEDIA_TYPE, request);
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleDataIntegrityViolation(
		DataIntegrityViolationException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("DataIntegrityViolationException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.DATA_INTEGRITY_VIOLATION, request);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleDataAccessException(
		DataAccessException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("DataAccessException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleAccessDeniedException(
		AccessDeniedException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("AccessDeniedException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED, request);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(MailException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleMailException(
		MailException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("MailException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.fail(response));
	}


	@ExceptionHandler(AuthException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleAuthException(
		AuthException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("AuthException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleUserException(
		UserException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("UserException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(AlertException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleAlertException(
		AlertException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("AlertException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(PostException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handlePostException(
		PostException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("PostException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(AiTaskException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleAiTaskException(
		AiTaskException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("AiTaskException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(TermsException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleTermsException(
		TermsException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("TermsException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(ImageException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleImageException(
		ImageException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("ImageException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(ProjectException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleProjectException(
		ProjectException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logException("ProjectException 발생", ex, request);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode(), request);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(BaseResponse.fail(response));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<ErrorResponse>> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpServletRequest request
	) {
		LoggingUtil.logValidationException(ex, request);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT, request);
		response.addValidationErrors(ex.getBindingResult());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(response));
	}

}