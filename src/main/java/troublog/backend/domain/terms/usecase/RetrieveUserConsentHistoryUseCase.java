package troublog.backend.domain.terms.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.service.query.UserTermsConsentQueryService;
import troublog.backend.domain.terms.validator.UserTermsConsentValidator;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RetrieveUserConsentHistoryUseCase {

	private final UserTermsConsentQueryService queryService;

	public List<UserTermsConsent> retrieveConsentHistory(Long userId) {
		List<UserTermsConsent> consentList = queryService.findAllByUserId(userId);
		consentList.forEach(userTermsConsent -> UserTermsConsentValidator.validate(userTermsConsent, userId));
		return consentList;
	}
}