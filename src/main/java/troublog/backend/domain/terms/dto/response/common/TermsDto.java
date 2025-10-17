package troublog.backend.domain.terms.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.terms.enums.TermsType;

@Builder
@Schema(description = "약관 정보 DTO")
public record TermsDto(
	@Schema(description = "약관 고유 ID", example = "1")
	Long id,

	@Schema(
		description = "약관 타입 (PRIVACY_POLICY: 개인정보처리방침, TERMS_OF_USE: 이용약관)",
		example = "PRIVACY"
	)
	TermsType termsType,

	@Schema(description = "약관 제목", example = "개인정보 처리방침")
	String title,

	@Schema(description = "약관 본문 내용", example = "제1조 (목적) 본 약관은...")
	String body,

	@Schema(
		description = "필수 동의 여부 (true: 필수, false: 선택)",
		example = "true"
	)
	Boolean isRequired,

	@Schema(
		description = "약관 동의 보관 기간 (년 단위)",
		example = "1"
	)
	Integer expirationPeriod

	) {
}
