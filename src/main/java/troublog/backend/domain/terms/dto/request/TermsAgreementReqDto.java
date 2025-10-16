package troublog.backend.domain.terms.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record TermsAgreementReqDto(
	@NotNull
	Map<Long, Boolean> termsAgreements
) {
}