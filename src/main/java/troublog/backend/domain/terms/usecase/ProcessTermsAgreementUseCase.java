package troublog.backend.domain.terms.usecase;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.global.common.error.exception.TermsException;
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
public class ProcessTermsAgreementUseCase {
	private final UserQueryService userQueryService;
	private final TermsQueryService termsQueryService;
	private final UserTermsConsentCommandService commandService;
	private final TermsFactory termsFactory;

	@Transactional
	public List<UserTermsConsent> processAgreement(Map<Long, Boolean> termsAgreements, Long userId) {
		User user = userQueryService.findUserById(userId);
		List<Terms> currentActiveTerms = termsQueryService.getCurrentActiveTerms();

		TermsValidator.validateTermsAgreement(currentActiveTerms, termsAgreements);

		return createUserTermsConsents(user, currentActiveTerms, termsAgreements);
	}

	private List<UserTermsConsent> createUserTermsConsents(
		User user,
		List<Terms> activeTerms,
		Map<Long, Boolean> agreements
	) {
		Map<Long, Terms> termsById = activeTerms.stream()
			.collect(Collectors.toMap(Terms::getId, terms -> terms));

		List<UserTermsConsent> consents = agreements.entrySet().stream()
			.map(entry -> {
				Terms terms = termsById.get(entry.getKey());
				if (Objects.isNull(terms)) {
					throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
				}
				return termsFactory.createUserTermsConsent(user, terms, entry.getValue());
			})
			.toList();

		return commandService.saveAll(consents);
	}
}