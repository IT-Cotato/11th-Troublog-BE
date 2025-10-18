package troublog.backend.domain.terms.facade.query;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.converter.TermsConverter;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.usecase.RetrieveActiveTermsUseCase;
import troublog.backend.domain.terms.usecase.RetrieveUserConsentHistoryUseCase;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsQueryFacadeImpl implements TermsQueryFacade {

	private final RetrieveActiveTermsUseCase retrieveActiveTermsUseCase;
	private final RetrieveUserConsentHistoryUseCase retrieveUserConsentHistoryUseCase;

	@Override
	public LatestTermsResDto getLatestTerms() {
		List<Terms> result = retrieveActiveTermsUseCase.retrieveActiveTerms();
		return TermsConverter.toLatestTermsResDto(result);
	}

	@Override
	public TermsAgreementResDto getUserTermsHistory(Long userId) {
		List<UserTermsConsent> result = retrieveUserConsentHistoryUseCase.retrieveConsentHistory(userId);
		return TermsConverter.toTermsAgreementResDto(result);
	}
}
