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
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "C-003", "유효하지 않은 입력값입니다."),

	/**
	 * Auth Error
	 */
	INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "AU-001", "secret key가 위조되거나 잘못된 토큰입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AU-002", "토큰이 만료되었습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AU-003", "유효하지 않은 토큰입니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AU-004", "인증이 필요합니다."),
	WRONG_ENVIRONMENT(HttpStatus.UNAUTHORIZED, "AU-005", "잘못된 환경으로 접근했습니다."),
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AU-006", "토큰을 찾을 수 없습니다."),
	TOKEN_NOT_EXPIRED(HttpStatus.CONFLICT, "AU-007", "만료되지 않은 토큰입니다."),

	/**
	 * User Error
	 */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U-001", "유저를 찾을 수 없습니다"),
	INVALID_USER(HttpStatus.NOT_FOUND, "U-002", "아이디 또는 비밀번호가 일치하지 않습니다."),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "U-003", "중복된 이메일입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "U-004", "중복된 닉네임입니다."),
	USER_DELETED(HttpStatus.BAD_REQUEST, "U-005", "삭제된 유저입니다."),
	USER_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "U-006", "자기자신을 팔로우/언팔로우 할 수 없습니다."),
	USER_UPDATE_SELF(HttpStatus.BAD_REQUEST, "U-007", "자기자신의 정보만 수정할 수 있습니다."),
	DUPLICATED_FOLLOWED(HttpStatus.CONFLICT, "U-008", "이미 팔로우한 유저입니다."),
	USER_NOT_FOLLOWED(HttpStatus.BAD_REQUEST, "U-009", "팔로우하지 않은 유저입니다."),
	MISSING_USER(HttpStatus.BAD_REQUEST, "U-0010", "사용자 정보가 누락되었습니다."),
	USER_STATUS_INVALID(HttpStatus.BAD_REQUEST, "U-0011", "유효하지 않은 유저 상태입니다."),
	USER_INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "U-0012", "유효하지 않은 닉네임입니다."),
	DUPLICATED_EMAIL_KAKAO(HttpStatus.CONFLICT, "U-0013", "이미 카카오로 가입한 이메일입니다."),

	/**
	 * Alert Error
	 */

	ALERT_NOT_FOUND(HttpStatus.NOT_FOUND, "AL-001", "알림을 찾을 수 없습니다."),
	ALERT_TYPE_INVALID(HttpStatus.BAD_REQUEST, "AL-002", "유효하지 않은 알림 타입입니다."),

	/**
	 * Project Error
	 */
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PR-001", "프로젝트를 찾지 못했습니다."),
	MISSING_PROJECT(HttpStatus.BAD_REQUEST, "PR-002", "프로젝트 정보가 누락되었습니다."),

	/**
	 * Post Error
	 */
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P-001", "트러블슈팅 문서를 찾지 못했습니다."),
	INVALID_VALUE(HttpStatus.NOT_FOUND, "P-002", "잘못된 상태값입니다."),
	MISSING_POST(HttpStatus.BAD_REQUEST, "P-003", "트러블 슈팅 문서가 누락되었습니다."),
	POST_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "P-004", "접근 불가능한 트러블슈팅 문서입니다."),
	POST_NOT_DELETED(HttpStatus.BAD_GATEWAY, "P-005", "삭제되지 않은 트러블슈팅 문서입니다."),
	POST_NOT_VISIBLE(HttpStatus.FORBIDDEN, "P-006", "공개되지 않은 게시물입니다."),
	JSON_PARSING_ERROR(HttpStatus.BAD_REQUEST, "J-007", "JSON 파싱 중 오류가 발생했습니다."),
	SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "P-008", "요약본을 찾을 수 없습니다."),

	/**
	 * Post Summary Error
	 */
	MISSING_POST_SUMMARY(HttpStatus.BAD_REQUEST, "PS-001", "요약본 정보가 누락되었습니다."),
	MISSING_SUMMARY_CONTENT(HttpStatus.BAD_REQUEST, "PS-002", "요약본 콘텐츠 정보가 누락되었습니다."),
	POST_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "PS-003", "요약본을 찾지 못했습니다."),
	USER_SUMMARY_MISMATCH(HttpStatus.FORBIDDEN, "PS-004", "해당 요약본과 소유자가 일치하지 않습니다."),
	/**
	 * Content Error
	 */
	CONTENT_LIST_IS_EMPTY(HttpStatus.BAD_REQUEST, "C-004", "콘텐츠 리스트가 비어있습니다."),
	MISSING_CONTENT_LIST(HttpStatus.BAD_REQUEST, "C-005", "콘텐츠 리스트가 누락되었습니다."),
	MISSING_CONTENT(HttpStatus.BAD_REQUEST, "C-006", "콘텐츠 정보가 누락되었습니다."),

	/**
	 * PostTag Error
	 */
	MISSING_POST_TAG_LIST(HttpStatus.BAD_REQUEST, "PT-001", "포스트 태그 리스트가 누락되었습니다."),
	MISSING_POST_TAG(HttpStatus.BAD_REQUEST, "PT-002", "포스트 태그 정보가 누락되었습니다."),

	/**
	 * Tag Error
	 */
	MISSING_ERROR_TAG(HttpStatus.BAD_REQUEST, "T-001", "에러 태그 정보가 누락되었습니다."),
	MISSING_TECH_STACK_INFORMATION(HttpStatus.BAD_REQUEST, "T-002", "필수 기술 스택 정보가 누락되었습니다."),
	MISSING_TAG(HttpStatus.BAD_REQUEST, "T-003", "필수 태그 정보가 누락되었습니다."),
	TECH_STACK_NOT_FOUND(HttpStatus.NOT_FOUND, "T-004", "기술 스택을 찾을 수 없습니다."),
	TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "T-005", "태그를 찾을 수 없습니다."),
	INVALID_TAG_TYPE(HttpStatus.BAD_REQUEST, "T-006", "유효하지 않은 태그 타입입니다."),

	/**
	 * Comment Error
	 */
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CM-001", "댓글를 찾지 못했습니다."),
	COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CM-002", "접근 불가능한 댓글입니다."),
	COMMENT_NOT_PARENT(HttpStatus.BAD_REQUEST, "CM-003", "상위 댓글이 아닙니다."),
	COMMENT_HAS_CHILDREN(HttpStatus.CONFLICT, "CM-004", "대댓글이 존재하는 댓글입니다."),

	/**
	 * Like Error
	 */
	LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "L-001", "이미 좋아요가 눌러진 상태입니다."),
	LIKE_NOT_EXISTS(HttpStatus.NOT_FOUND, "L-002", "좋아요가 되어 있지 않은 상태입니다."),
	MISSING_LIKE(HttpStatus.NOT_FOUND, "L-003", "좋아요가 생성되지 않았습니다."),

	/**
	 * Image Error
	 */
	MISSING_IMAGE_LIST(HttpStatus.BAD_REQUEST, "I-001", "이미지 리스트가 누락되었습니다."),
	MISSING_IMAGE(HttpStatus.BAD_REQUEST, "I-002", "이미지 정보가 누락되었습니다."),

	/**
	 * Server Error
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "서버 오류가 발생했습니다."),

	/**
	 * Image  Error
	 */
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "I-001", "이미지를 찾지 못했습니다."),
	IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "I-002", "이미지 업로드에 실패했습니다."),
	IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "I-003", "이미지 삭제에 실패했습니다."),
	URL_NOT_VALID(HttpStatus.BAD_REQUEST, "I-004", "잘못된 이미지 URL 입니다."),
	FILE_SIZE_EXCEEDING(HttpStatus.BAD_REQUEST, "I-005", "지정된 파일크기를 초과했습니다."),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "I-006", "파일을 찾을 수 없습니다."),

	/**
	 * AI  Error
	 */
	TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "AI-001", "작업을 찾을 수 없습니다."),
	TASK_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "AI-002", "잘못된 작업 업데이트 요청입니다."),
	TASK_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "AI-003", "이미 완료된 작업입니다."),
	PROMPT_NOT_FOUND(HttpStatus.NOT_FOUND, "AI-004", "프롬프트를 찾을 수 없습니다."),
	TASK_POST_MISMATCH(HttpStatus.BAD_REQUEST, "AI-005", "작업과 게시물이 일치하지 않습니다."),

	/**
	 * Terms Error
	 */
	MISSING_TERMS(HttpStatus.BAD_REQUEST, "TR-001", "약관을 찾을 수 없습니다."),
	CANNOT_DELETE_CURRENT_TERMS(HttpStatus.BAD_REQUEST, "TR-002", "최신 버전 약관을 삭제할 수 없습니다."),
	TERMS_NOT_FOUND(HttpStatus.NOT_FOUND, "TR-003", "약관을 찾을 수 없습니다."),
	TERMS_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "TR-004", "약관 제목은 필수입니다."),
	TERMS_BODY_REQUIRED(HttpStatus.BAD_REQUEST, "TR-005", "약관 본문은 필수입니다."),
	TERMS_NOT_CURRENT(HttpStatus.BAD_REQUEST, "TR-006", "현재 버전이 아닌 약관입니다."),
	TERMS_DELETED(HttpStatus.BAD_REQUEST, "TR-007", "삭제된 약관입니다."),
	INVALID_CONSENT_DETAILS(HttpStatus.BAD_REQUEST, "TR-008", "유효하지 않은 동의 내역입니다."),
	REQUIRED_TERMS_NOT_AGREED(HttpStatus.BAD_REQUEST, "TR-009", "필수 약관에 동의하지 않았습니다."),
	NO_ACTIVE_TERMS(HttpStatus.NOT_FOUND, "TR-010", "활성화된 약관을 찾지 못했습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
