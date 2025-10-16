package troublog.backend.domain.terms.dto.response.common;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import troublog.backend.domain.terms.enums.TermsType;

@Schema(description = "사용자 약관 동의 정보")
public record UserConsentDto(
	@Schema(description = "사용자 ID", example = "1")
	Long userId,

	@Schema(description = "약관 ID", example = "1")
	Long termsId,

	@Schema(description = "약관 유형", example = "PRIVACY")
	TermsType termsType,

	@Schema(description = "동의 여부", example = "true")
	Boolean isAgreed,

	@Schema(description = "동의 일시", example = "2024-01-01T12:00:00")
	LocalDateTime agreedAt
) {
}
