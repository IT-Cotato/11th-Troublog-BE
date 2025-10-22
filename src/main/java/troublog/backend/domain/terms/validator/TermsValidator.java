package troublog.backend.domain.terms.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.exception.TermsException;
import troublog.backend.domain.terms.service.query.TermsQueryService;
import troublog.backend.global.common.error.ErrorCode;

@UtilityClass
public class TermsValidator {
	public void validate(Terms terms) {
		validateTermsNotNull(terms);
		validateTitle(terms.getTitle());
		validateContent(terms.getBody());
		validateActiveTerms(terms);
	}

	public void validateTermsAgreement(List<Terms> activeTerms, Map<Long, Boolean> agreements) {
		validateActiveTermsExist(activeTerms);
		activeTerms.forEach(TermsValidator::validate);
		validateAllTermsProvided(activeTerms, agreements);
		validateRequiredTermsAgreed(activeTerms, agreements);
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

	public void validateRequiredTermsAgreement(Map<Long, Boolean> termsAgreements, Terms requiredTerm) {
		if (Boolean.FALSE.equals(termsAgreements.getOrDefault(requiredTerm.getId(), false))) {
			throw new TermsException(ErrorCode.REQUIRED_TERMS_NOT_AGREED);
		}
	}

	public void validateAllTermsProvided(List<Terms> currentActiveTerms, Map<Long, Boolean> termsAgreements) {
		HashSet<Long> currentTermsIds = currentActiveTerms.stream()
			.map(Terms::getId)
			.collect(Collectors.toCollection(HashSet::new));

		boolean allIdsMatch = currentTermsIds.containsAll(termsAgreements.keySet());

		if (!allIdsMatch) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
	}

	public void validateForRegistration(Map<Long, Boolean> termsAgreements, TermsQueryService termsQueryService) {
		List<Terms> currentActiveTerms = termsQueryService.getCurrentActiveTerms();
		validateEmptyTerms(currentActiveTerms, termsAgreements);
		validateAllTermsProvided(currentActiveTerms, termsAgreements);

		currentActiveTerms.stream()
			.filter(Terms::getIsRequired)
			.forEach(requiredTerm -> validateRequiredTermsAgreement(termsAgreements, requiredTerm));
	}

	private static void validateEmptyTerms(List<Terms> currentActiveTerms, Map<Long, Boolean> termsAgreements) {
		if (CollectionUtils.isEmpty(termsAgreements) || CollectionUtils.isEmpty(currentActiveTerms)) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
	}


	public void validateActiveTermsExist(List<Terms> currentActiveTerms) {
		if (CollectionUtils.isEmpty(currentActiveTerms)) {
			throw new TermsException(ErrorCode.NO_ACTIVE_TERMS);
		}
	}


	public void validateRequiredTermsAgreed(List<Terms> activeTerms, Map<Long, Boolean> agreements) {
		List<Terms> missingRequired = activeTerms.stream()
			.filter(Terms::getIsRequired)
			.filter(term -> !Boolean.TRUE.equals(agreements.get(term.getId())))
			.toList();

		if (!missingRequired.isEmpty()) {
			throw new TermsException(ErrorCode.REQUIRED_TERMS_NOT_AGREED);
		}
	}
}
