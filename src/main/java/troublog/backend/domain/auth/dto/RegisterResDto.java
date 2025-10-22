package troublog.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;

@Builder
@Schema(description = "회원가입 응답 DTO")
public record RegisterResDto(

	@Schema(description = "사용자 ID")
	@JsonProperty("userId")
	Long userId,

	@Schema(description = "이용약관 동의 내역")
	@JsonProperty("termsAgreement")
	TermsAgreementResDto termsAgreement
) {}
