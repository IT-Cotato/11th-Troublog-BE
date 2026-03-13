package troublog.backend.domain.terms.service.facade;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.converter.TermsConverter;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.factory.TermsFactory;
import troublog.backend.domain.terms.service.command.UserTermsConsentCommandService;
import troublog.backend.domain.terms.service.query.TermsQueryService;
import troublog.backend.domain.terms.validator.TermsValidator;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.TermsException;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsCommandFacadeService {

	private final UserQueryService userQueryService;
	private final TermsQueryService termsQueryService;
	private final UserTermsConsentCommandService userTermsConsentCommandService;
	private final TermsFactory termsFactory;

	@Transactional
	public TermsAgreementResDto agreeToTerms(final Map<Long, Boolean> termsAgreements, final Long userId) {
		User user = userQueryService.findUserById(userId);
		List<Terms> currentActiveTerms = termsQueryService.getCurrentActiveTerms();

		TermsValidator.validateTermsAgreement(currentActiveTerms, termsAgreements);

		List<UserTermsConsent> result = createUserTermsConsents(user, currentActiveTerms, termsAgreements);
		return TermsConverter.toTermsAgreementResDto(result);
	}

	private List<UserTermsConsent> createUserTermsConsents(
		final User user,
		final List<Terms> activeTerms,
		final Map<Long, Boolean> agreements
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

		return userTermsConsentCommandService.saveAll(consents);
	}
}
