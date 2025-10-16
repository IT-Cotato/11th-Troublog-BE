package troublog.backend.domain.terms.facade.query;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.mapper.TermsMapper;
import troublog.backend.domain.terms.usecase.GetLatestTermsUseCase;
import troublog.backend.domain.terms.usecase.GetUserTermsHistoryUseCase;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsQueryFacadeImpl implements TermsQueryFacade {

	private final GetLatestTermsUseCase getLatestTermsUseCase;
	private final GetUserTermsHistoryUseCase getUserTermsHistoryUseCase;

	@Override
	public LatestTermsResDto getLatestTerms() {
		List<Terms> result = getLatestTermsUseCase.execute();
		return TermsMapper.INSTANCE.toLatestTermsResDto(result);
	}

	@Override
	public TermsAgreementResDto getUserTermsHistory(Long userId) {
		List<UserTermsConsent> result = getUserTermsHistoryUseCase.execute(userId);
		return TermsMapper.INSTANCE.toTermsAgreementResDto(result);
	}
}
