package troublog.backend.domain.terms.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.terms.dto.response.common.UserConsentDto;

@Builder
@Schema(description = "약관 동의 응답 DTO - 사용자의 약관 동의 처리 결과 또는 동의 이력 조회 결과")
public record TermsAgreementResDto(
	@Schema(
		description = "사용자가 동의한 약관 목록. 회원가입 시 저장된 동의 내역 또는 사용자의 약관 동의 이력입니다. " +
			"각 항목에는 동의 일시, 약관 타입, 동의 여부 등의 정보가 포함됩니다."
	)
	List<UserConsentDto> userConsentDtos
) {
}