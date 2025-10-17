package troublog.backend.domain.terms.validator;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.exception.TermsException;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@UtilityClass
public class UserTermsConsentValidator {

	public void validate(UserTermsConsent userTermsConsent, Long userId) {
		validateUserId(userId);
		validateConsentNotNull(userTermsConsent);
		validateTerms(userTermsConsent);
		validateUser(userTermsConsent);
		validateUserConsentDate(userTermsConsent);
		validateTermsConsentBelongToUser(userTermsConsent, userId);
		validateAgreeToRequiredItems(userTermsConsent);
	}

	private static void validateUserId(Long userId) {
		if (Objects.isNull(userId)) {
			throw new UserException(ErrorCode.USER_NOT_FOUND);
		}
	}

	private static void validateConsentNotNull(UserTermsConsent userTermsConsent) {
		if (Objects.isNull(userTermsConsent)) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
	}

	private static void validateTerms(UserTermsConsent userTermsConsent) {
		if (Objects.isNull(userTermsConsent.getTerms())) {
			throw new TermsException(ErrorCode.TERMS_NOT_FOUND);
		}
	}

	private static void validateUser(UserTermsConsent userTermsConsent) {
		if (Objects.isNull(userTermsConsent.getUser())) {
			throw new TermsException(ErrorCode.USER_NOT_FOUND);
		}
	}

	private static void validateTermsConsentBelongToUser(UserTermsConsent userTermsConsent, Long userId) {
		if (!userTermsConsent.getUser().getId().equals(userId)) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
	}

	private static void validateUserConsentDate(UserTermsConsent userTermsConsent) {
		LocalDateTime agreedAt = userTermsConsent.getAgreedAt();
		if (Objects.isNull(agreedAt)) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
		if (agreedAt.isAfter(LocalDateTime.now())) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
	}

	private static void validateAgreeToRequiredItems(UserTermsConsent userTermsConsent) {
		if (Boolean.TRUE.equals(userTermsConsent.getTerms().getIsRequired())) {
			if (Boolean.FALSE.equals(userTermsConsent.getIsAgreed())) {
				throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
			}
			if (Objects.isNull(userTermsConsent.getAgreedAt())) {
				throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
			}
		}
	}
}
