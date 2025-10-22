package troublog.backend.domain.terms.facade.command;

import java.util.Map;

import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;

public interface TermsCommandFacade {

	TermsAgreementResDto agreeToTerms(Map<Long, Boolean> termsAgreements, Long userId);
}
