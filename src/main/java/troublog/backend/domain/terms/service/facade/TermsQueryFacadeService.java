package troublog.backend.domain.terms.service.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.converter.TermsConverter;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.service.query.TermsQueryService;
import troublog.backend.domain.terms.service.query.UserTermsConsentQueryService;
import troublog.backend.domain.terms.validator.TermsValidator;
import troublog.backend.domain.terms.validator.UserTermsConsentValidator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsQueryFacadeService {

	private final TermsQueryService termsQueryService;
	private final UserTermsConsentQueryService userTermsConsentQueryService;

	public LatestTermsResDto getLatestTerms() {
		List<Terms> currentActiveTerms = termsQueryService.getCurrentActiveTerms();
		currentActiveTerms.forEach(TermsValidator::validate);
		return TermsConverter.toLatestTermsResDto(currentActiveTerms);
	}

	public TermsAgreementResDto getUserTermsHistory(final Long userId) {
		List<UserTermsConsent> consentList = userTermsConsentQueryService.findAllByUserId(userId);
		consentList.forEach(userTermsConsent -> UserTermsConsentValidator.validate(userTermsConsent, userId));
		return TermsConverter.toTermsAgreementResDto(consentList);
	}
}
