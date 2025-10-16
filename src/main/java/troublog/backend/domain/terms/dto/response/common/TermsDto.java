package troublog.backend.domain.terms.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;
import troublog.backend.domain.terms.enums.TermsType;

@Schema(description = "약관 정보")
public record TermsDto(
	@Schema(description = "약관 ID", example = "1")
	Long id,

	@Schema(description = "약관 타입", example = "TERMS_OF_SERVICE")
	TermsType termsType,

	@Schema(description = "약관 제목", example = "서비스 이용약관")
	String title,

	@Schema(description = "약관 본문", example = "제1조 (목적) 본 약관은...")
	String body,

	@Schema(description = "필수 동의 여부", example = "true")
	Boolean isRequired
) {
}
