package troublog.backend.domain.terms.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.exception.TermsException;
import troublog.backend.domain.terms.service.command.UserTermsConsentCommandService;
import troublog.backend.domain.terms.service.query.TermsQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.error.ErrorCode;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreeToTermsUseCase {
	private final UserQueryService userQueryService;
	private final TermsQueryService termsQueryService;
	private final UserTermsConsentCommandService userTermsConsentCommandService;

	public List<UserTermsConsent> execute(Map<Long, Boolean> termsAgreements, Long userId) {
		if (CollectionUtils.isEmpty(termsAgreements)) {
			throw new TermsException(ErrorCode.INVALID_CONSENT_DETAILS);
		}
		User user = userQueryService.findUserById(userId);

		List<UserTermsConsent> userTermsConsentList = new ArrayList<>();
		//TODO EventListner를 이용하여 회원 가입 로직 실행시 이용약관 내역 저장
		return userTermsConsentCommandService.saveAll(userTermsConsentList);
	}
}
