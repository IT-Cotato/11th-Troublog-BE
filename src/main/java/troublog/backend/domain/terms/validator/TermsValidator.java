package troublog.backend.domain.terms.validator;

import java.util.Objects;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.exception.TermsException;
import troublog.backend.global.common.error.ErrorCode;

@UtilityClass
public class TermsValidator {
	public void validate(Terms terms) {
		validateTermsNotNull(terms);
		validateTitle(terms.getTitle());
		validateContent(terms.getBody());
		validateActiveTerms(terms);
	}

	private void validateTermsNotNull(Terms terms) {
		if (Objects.isNull(terms)) {
			throw new TermsException(ErrorCode.TERMS_NOT_FOUND);
		}
	}

	private void validateTitle(final String title) {
		if (Objects.isNull(title) || title.isBlank()) {
			throw new TermsException(ErrorCode.TERMS_TITLE_REQUIRED);
		}
	}

	private void validateContent(String body) {
		if (Objects.isNull(body) || body.isBlank()) {
			throw new TermsException(ErrorCode.TERMS_BODY_REQUIRED);
		}
	}

	private void validateActiveTerms(Terms terms) {
		if (Boolean.FALSE.equals(terms.getIsCurrent())) {
			throw new TermsException(ErrorCode.TERMS_NOT_CURRENT);
		} else if (Boolean.TRUE.equals(terms.getIsDeleted())) {
			throw new TermsException(ErrorCode.TERMS_DELETED);
		}
	}
}
