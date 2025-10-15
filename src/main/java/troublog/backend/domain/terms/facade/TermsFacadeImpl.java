package troublog.backend.domain.terms.facade;

import troublog.backend.domain.terms.dto.request.TermsAgreementReqDto;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.dto.response.UserTermsHistoryResDto;

public class TermsFacadeImpl implements TermsFacade {

	@Override
	public LatestTermsResDto getLatestTerms() {
		// TODO: 최신 버전의 이용약관 및 개인정보처리방침 조회
		return null;
	}

	@Override
	public TermsAgreementResDto agreeToTerms(TermsAgreementReqDto request, Long userId) {
		// TODO: 사용자의 약관 동의 정보 저장 및 처리
		return null;
	}

	@Override
	public UserTermsHistoryResDto getUserTermsHistory(Long userId) {
		// TODO: 특정 사용자의 약관 동의 이력 조회
		return null;
	}
}
