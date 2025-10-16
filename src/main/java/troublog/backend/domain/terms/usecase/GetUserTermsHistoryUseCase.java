package troublog.backend.domain.terms.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.repository.UserTermsConsentRepository;
import troublog.backend.domain.terms.validator.UserTermsConsentValidator;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserTermsHistoryUseCase {

	private final UserTermsConsentRepository repository;

	public List<UserTermsConsent> execute(Long userId) {
		List<UserTermsConsent> consentList = repository.findAllByUserId(userId);
		consentList.forEach(userTermsConsent -> UserTermsConsentValidator.validate(userTermsConsent, userId));
		return consentList;
	}
}
