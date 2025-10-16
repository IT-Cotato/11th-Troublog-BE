package troublog.backend.domain.terms.facade.command;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.mapper.TermsMapper;
import troublog.backend.domain.terms.usecase.AgreeToTermsUseCase;

@Component
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsCommandFacadeImpl implements TermsCommandFacade {

	private final AgreeToTermsUseCase agreeToTermsUseCase;

	@Override
	public TermsAgreementResDto agreeToTerms(Map<Long, Boolean> termsAgreements, Long userId) {
		List<UserTermsConsent> result = agreeToTermsUseCase.execute(termsAgreements, userId);
		return TermsMapper.INSTANCE.toTermsAgreementResDto(result);
	}
}
