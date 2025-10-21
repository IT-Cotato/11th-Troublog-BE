package troublog.backend.domain.terms.dto.response.common;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.terms.enums.TermsType;

@Builder
@Schema(description = "사용자 약관 동의 정보 DTO")
public record UserConsentDto(
	@Schema(description = "동의한 사용자의 고유 ID", example = "1")
	Long userId,

	@Schema(description = "동의한 약관의 고유 ID", example = "1")
	Long termsId,

	@Schema(
		description = "약관 타입 (PRIVACY_POLICY: 개인정보처리방침, TERMS_OF_USE: 이용약관)",
		example = "PRIVACY_POLICY"
	)
	TermsType termsType,

	@Schema(
		description = "동의 여부 (true: 동의함, false: 동의하지 않음)",
		example = "true"
	)
	Boolean isAgreed,

	@Schema(
		description = "약관 동의 일시",
		example = "2024-01-15T10:30:00"
	)
	LocalDateTime agreedAt,

	@Schema(
		description = "약관 동의 만료 일시 (보관 기간 종료 시점)",
		example = "2025-01-15T10:30:00"
	)
	LocalDateTime expirationAt
) {
}
