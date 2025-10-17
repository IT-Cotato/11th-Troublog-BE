package troublog.backend.domain.terms.factory;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.user.entity.User;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsFactory {

	public UserTermsConsent createUserTermsConsent(User user, Terms terms, Boolean isAgreed) {
		LocalDateTime now = LocalDateTime.now();
		return UserTermsConsent.builder()
			.user(user)
			.terms(terms)
			.isAgreed(isAgreed)
			.isCurrent(true)
			.agreedAt(now)
			.termsType(terms.getTermsType())
			.expirationAt(terms.getExpirationPeriod() != null ?
				now.plusYears(terms.getExpirationPeriod()) : null)
			.build();
	}
}
