package troublog.backend.global.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * Common Error (C-xxx)
	 */
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "C-001", "잘못된 요청입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "C-002", "리소스를 찾을 수 없습니다."),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "C-003", "유효하지 않은 입력값입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C-004", "서버 오류가 발생했습니다."),
	JSON_PARSING_ERROR(HttpStatus.BAD_REQUEST, "C-005", "JSON 파싱 중 오류가 발생했습니다."),
	TEMPLATE_LOADING_FAILED(HttpStatus.NOT_FOUND, "C-006", "템플릿 로딩에 실패했습니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "C-007", "요청한 리소스에 접근할 수 없습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C-008", "지원하지 않는 HTTP 메서드입니다."),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "C-009", "지원하지 않는 미디어 타입입니다."),
	DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "C-010", "데이터 무결성 위반입니다."),

	/**
	 * Auth Error (AU-xxx)
	 */
	INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "AU-001", "secret key가 위조되거나 잘못된 토큰입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AU-002", "토큰이 만료되었습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AU-003", "유효하지 않은 토큰입니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AU-004", "인증이 필요합니다."),
	WRONG_ENVIRONMENT(HttpStatus.UNAUTHORIZED, "AU-005", "잘못된 환경으로 접근했습니다."),
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AU-006", "토큰을 찾을 수 없습니다."),
	TOKEN_NOT_EXPIRED(HttpStatus.CONFLICT, "AU-007", "만료되지 않은 토큰입니다."),
	AUTH_CODE_EMPTY(HttpStatus.NOT_FOUND, "AU-008", "존재하지 않는 인증코드입니다."),
	AUTH_CODE_NOT_LATEST(HttpStatus.BAD_REQUEST, "AU-009", "가장 최근에 전송한 인증코드를 입력해주세요."),
	AUTH_CODE_INVALID(HttpStatus.BAD_REQUEST, "AU-010", "유효하지 않은 인증코드입니다."),

	/**
	 * User Error (U-xxx)
	 */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U-001", "유저를 찾을 수 없습니다."),
	USER_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "U-002", "사용자의 좋아요 정보가 누락되었습니다."),
	INVALID_USER(HttpStatus.NOT_FOUND, "U-003", "아이디 또는 비밀번호가 일치하지 않습니다."),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "U-004", "중복된 이메일입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "U-005", "중복된 닉네임입니다."),
	USER_DELETED(HttpStatus.BAD_REQUEST, "U-006", "삭제된 유저입니다."),
	USER_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "U-007", "자기자신을 팔로우/언팔로우 할 수 없습니다."),
	USER_UPDATE_SELF(HttpStatus.BAD_REQUEST, "U-008", "자기자신의 정보만 수정할 수 있습니다."),
	DUPLICATED_FOLLOWED(HttpStatus.CONFLICT, "U-009", "이미 팔로우한 유저입니다."),
	USER_NOT_FOLLOWED(HttpStatus.BAD_REQUEST, "U-010", "팔로우하지 않은 유저입니다."),
	MISSING_USER(HttpStatus.BAD_REQUEST, "U-011", "사용자 정보가 누락되었습니다."),
	USER_STATUS_INVALID(HttpStatus.BAD_REQUEST, "U-012", "유효하지 않은 유저 상태입니다."),
	USER_INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "U-013", "유효하지 않은 닉네임입니다."),
	USER_EMAIL_INVALID(HttpStatus.BAD_REQUEST, "U-014", "존재하지 않는 이메일입니다."),

	/**
	 * Alert Error (AL-xxx)
	 */
	ALERT_NOT_FOUND(HttpStatus.NOT_FOUND, "AL-001", "알림을 찾을 수 없습니다."),
	ALERT_TYPE_INVALID(HttpStatus.BAD_REQUEST, "AL-002", "유효하지 않은 알림 타입입니다."),
	ALERT_SELF_DELETE_CHECK(HttpStatus.BAD_REQUEST, "AL-003", "자기자신의 알람만 삭제할 수 있습니다."),

	/**
	 * Project Error (PR-xxx)
	 */
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PR-001", "프로젝트를 찾지 못했습니다."),
	MISSING_PROJECT(HttpStatus.BAD_REQUEST, "PR-002", "프로젝트 정보가 누락되었습니다."),
	PROJECT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "PR-003", "접근 불가능한 프로젝트입니다."),

	/**
	 * Post Error (P-xxx)
	 */
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P-001", "트러블슈팅 문서를 찾지 못했습니다."),
	POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "P-002", "접근 불가능한 트러블슈팅 문서입니다."),
	POST_NOT_DELETED(HttpStatus.BAD_GATEWAY, "P-003", "삭제되지 않은 트러블슈팅 문서입니다."),
	MISSING_POST(HttpStatus.BAD_REQUEST, "P-004", "트러블 슈팅 문서가 누락되었습니다."),
	INVALID_VALUE(HttpStatus.BAD_REQUEST, "P-005", "잘못된 값입니다."),

	/**
	 * Post Summary Error (PS-xxx)
	 */
	POST_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "PS-001", "요약본을 찾지 못했습니다."),
	MISSING_POST_SUMMARY(HttpStatus.BAD_REQUEST, "PS-002", "요약본 정보가 누락되었습니다."),
	MISSING_SUMMARY_CONTENT(HttpStatus.BAD_REQUEST, "PS-003", "요약본 콘텐츠 정보가 누락되었습니다."),
	MISSING_SUMMARY_TYPE(HttpStatus.BAD_REQUEST, "PS-004", "잘못된 요약본 타입입니다."),
	USER_SUMMARY_MISMATCH(HttpStatus.FORBIDDEN, "PS-005", "해당 요약본과 소유자가 일치하지 않습니다."),

	/**
	 * Content Error (CT-xxx)
	 */
	MISSING_CONTENT(HttpStatus.BAD_REQUEST, "CT-001", "콘텐츠 정보가 누락되었습니다."),
	SEQUENCE_NOT_VALID(HttpStatus.BAD_REQUEST, "CT-002", "시퀀스 넘버가 잘못되었습니다."),

	/**
	 * Tag Error (T-xxx)
	 */
	TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "T-001", "태그를 찾을 수 없습니다."),
	MISSING_TAG(HttpStatus.BAD_REQUEST, "T-002", "필수 태그 정보가 누락되었습니다."),
	MISSING_ERROR_TAG(HttpStatus.BAD_REQUEST, "T-003", "에러 태그 정보가 누락되었습니다."),
	MISSING_POST_TAG(HttpStatus.BAD_REQUEST, "T-004", "포스트 태그 정보가 누락되었습니다."),

	/**
	 * Comment Error (CM-xxx)
	 */
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CM-001", "댓글을 찾지 못했습니다."),
	COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CM-002", "접근 불가능한 댓글입니다."),
	COMMENT_NOT_PARENT(HttpStatus.BAD_REQUEST, "CM-003", "상위 댓글이 아닙니다."),
	COMMENT_HAS_CHILDREN(HttpStatus.CONFLICT, "CM-004", "대댓글이 존재하는 댓글입니다."),

	/**
	 * Report Error (R-xxx)
	 */
	REPORT_ALREADY_EXISTS(HttpStatus.CONFLICT, "R-001", "이미 신고한 대상입니다."),
	REPORT_COPYRIGHT_IMG(HttpStatus.BAD_REQUEST, "R-002", "저작권 침해 신고 시에만 서류첨부가 가능합니다."),
	REPORT_TARGET_USER_MISMATCH(HttpStatus.BAD_REQUEST, "R-003", "신고 대상 사용자 정보가 일치하지 않습니다."),

	/**
	 * Like Error (L-xxx)
	 */
	MISSING_LIKE(HttpStatus.NOT_FOUND, "L-001", "좋아요가 생성되지 않았습니다."),

	/**
	 * Image Error (I-xxx)
	 */
	IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "I-001", "이미지 업로드에 실패했습니다."),
	IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "I-002", "이미지 삭제에 실패했습니다."),
	URL_NOT_VALID(HttpStatus.BAD_REQUEST, "I-003", "잘못된 이미지 URL 입니다."),
	FILE_SIZE_EXCEEDING(HttpStatus.BAD_REQUEST, "I-004", "지정된 파일크기를 초과했습니다."),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "I-005", "파일을 찾을 수 없습니다."),

	/**
	 * AI Task Error (AI-xxx)
	 */
	TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "AI-001", "작업을 찾을 수 없습니다."),
	TASK_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "AI-002", "잘못된 작업 업데이트 요청입니다."),
	TASK_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "AI-003", "이미 완료된 작업입니다."),
	TASK_POST_MISMATCH(HttpStatus.BAD_REQUEST, "AI-004", "작업과 게시물이 일치하지 않습니다."),
	CHAT_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "AI-005", "Chat Model을 찾을 수 없습니다."),

	/**
	 * Mail Error (M-xxx)
	 */
	MAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "M-001", "메일 전송에 실패했습니다."),
	MAIL_TEMPLATE_LOADING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "M-002", "메일 템플릿 로딩에 실패했습니다."),

	/**
	 * Terms Error (TR-xxx)
	 */
	TERMS_NOT_FOUND(HttpStatus.NOT_FOUND, "TR-001", "약관을 찾을 수 없습니다."),
	CANNOT_DELETE_CURRENT_TERMS(HttpStatus.BAD_REQUEST, "TR-002", "최신 버전 약관을 삭제할 수 없습니다."),
	TERMS_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "TR-003", "약관 제목은 필수입니다."),
	TERMS_BODY_REQUIRED(HttpStatus.BAD_REQUEST, "TR-004", "약관 본문은 필수입니다."),
	TERMS_NOT_CURRENT(HttpStatus.BAD_REQUEST, "TR-005", "현재 버전이 아닌 약관입니다."),
	TERMS_DELETED(HttpStatus.BAD_REQUEST, "TR-006", "삭제된 약관입니다."),
	INVALID_CONSENT_DETAILS(HttpStatus.BAD_REQUEST, "TR-007", "유효하지 않은 동의 내역입니다."),
	REQUIRED_TERMS_NOT_AGREED(HttpStatus.BAD_REQUEST, "TR-008", "필수 약관에 동의하지 않았습니다."),
	NO_ACTIVE_TERMS(HttpStatus.NOT_FOUND, "TR-009", "활성화된 약관을 찾지 못했습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
