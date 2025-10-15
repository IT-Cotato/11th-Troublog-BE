package troublog.backend.domain.terms.facade;

import troublog.backend.domain.terms.dto.request.TermsAgreementReqDto;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.dto.response.UserTermsHistoryResDto;

public interface TermsFacade {

	TermsAgreementResDto agreeToTerms(TermsAgreementReqDto request, Long userId);

	LatestTermsResDto getLatestTerms();

	UserTermsHistoryResDto getUserTermsHistory(Long userId);
}
