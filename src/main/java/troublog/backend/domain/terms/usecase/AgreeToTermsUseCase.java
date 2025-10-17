package troublog.backend.domain.terms.usecase;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.exception.TermsException;
import troublog.backend.domain.terms.factory.TermsFactory;
import troublog.backend.domain.terms.service.command.UserTermsConsentCommandService;
import troublog.backend.domain.terms.service.query.TermsQueryService;
import troublog.backend.domain.terms.validator.TermsValidator;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.error.ErrorCode;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreeToTermsUseCase {
	private final UserQueryService userQueryService;
	private final TermsQueryService termsQueryService;
	private final UserTermsConsentCommandService commandService;
	private final TermsFactory termsFactory;

	@Transactional
	public List<UserTermsConsent> execute(Map<Long, Boolean> termsAgreements, Long userId) {
		if (CollectionUtils.isEmpty(termsAgreements)) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
		User user = userQueryService.findUserById(userId);

		List<Terms> currentActiveTerms = termsQueryService.getCurrentActiveTerms();
		currentActiveTerms.forEach(TermsValidator::validate);
		validateTermsIds(currentActiveTerms, termsAgreements);

		List<UserTermsConsent> userTermsConsentList = termsAgreements.entrySet().stream()
			.map(entry -> {
				Terms terms = currentActiveTerms.stream()
					.filter(t -> t.getId().equals(entry.getKey()))
					.findFirst()
					.orElseThrow(() -> new TermsException(ErrorCode.INVALID_CONSENT_DETAILS));

				return termsFactory.createUserTermsConsent(user, terms, entry.getValue());
			})
			.toList();

		return commandService.saveAll(userTermsConsentList);
	}

	private void validateTermsIds(List<Terms> currentActiveTerms, Map<Long, Boolean> termsAgreements) {
		HashSet<Long> currentTermsIds = currentActiveTerms.stream()
			.map(Terms::getId)
			.collect(Collectors.toCollection(HashSet::new));

		boolean allIdsMatch = currentTermsIds.containsAll(termsAgreements.keySet());

		if (!allIdsMatch) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
	}
}
