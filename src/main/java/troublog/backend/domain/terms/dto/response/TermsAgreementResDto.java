package troublog.backend.domain.terms.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.terms.dto.response.common.UserConsentDto;

@Builder
@Schema(description = "약관 동의 응답")
public record TermsAgreementResDto(
	@Schema(description = "사용자 약관 동의 목록")
	List<UserConsentDto> userConsentDtos
) {
}