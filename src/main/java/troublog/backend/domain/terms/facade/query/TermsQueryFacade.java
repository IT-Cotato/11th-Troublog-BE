package troublog.backend.domain.terms.facade.query;

import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;

public interface TermsQueryFacade {
	LatestTermsResDto getLatestTerms();

	TermsAgreementResDto getUserTermsHistory(Long userId);
}
