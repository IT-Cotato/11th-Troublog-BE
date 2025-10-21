package troublog.backend.domain.terms.facade.command;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.converter.TermsConverter;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.usecase.ProcessTermsAgreementUseCase;

@Component
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsCommandFacadeImpl implements TermsCommandFacade {

	private final ProcessTermsAgreementUseCase processTermsAgreementUseCase;

	@Override
	public TermsAgreementResDto agreeToTerms(Map<Long, Boolean> termsAgreements, Long userId) {
		List<UserTermsConsent> result = processTermsAgreementUseCase.processAgreement(termsAgreements, userId);
		return TermsConverter.toTermsAgreementResDto(result);
	}
}
