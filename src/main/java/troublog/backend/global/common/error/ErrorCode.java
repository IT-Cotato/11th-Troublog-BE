package troublog.backend.global.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * Common Error
	 */
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "C-001", "잘못된 요청입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "C-002", "리소스를 찾을 수 없습니다."),
	INVALID_INPUT(HttpStatus.BAD_REQUEST,"C-003", "유효하지 않은 입력값입니다."),

	/**
	 * Auth Error
	 */
	INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "a-001", "secret key가 위조되거나 잘못된 토큰입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "a-002", "토큰이 만료되었습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "a-003", "유효하지 않은 토큰입니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "a-004", "인증이 필요합니다."),
	WRONG_ENVIRONMENT(HttpStatus.UNAUTHORIZED, "a-005", "잘못된 환경으로 접근했습니다."),
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "a-006", "토큰을 찾을 수 없습니다."),
	TOKEN_NOT_EXPIRED(HttpStatus.CONFLICT, "a-007", "만료되지 않은 토큰입니다."),

	/**
	 * User Error
	 */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "u-001", "유저를 찾을 수 없습니다"),
	INVALID_USER(HttpStatus.NOT_FOUND, "u-002", "아이디 또는 비밀번호가 일치하지 않습니다."),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "u-003", "중복된 이메일입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "u-004", "중복된 닉네임입니다."),

	/**
	 * Server Error
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 오류가 발생했습니다."),

	/**
	 * Post error
	 */
	POST_NOT_FOUND(HttpStatus.NOT_FOUND,"P-001" , "트러블슈팅 문서를 찾지 못했습니다."),
	INVALID_VALUE(HttpStatus.NOT_FOUND,"P-002" , "잘못된 상태값입니다."),

	/**
	 * Project error
	 */
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND,"PR-001" ,"프로젝트를 찾지 못했습니다.");
	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
