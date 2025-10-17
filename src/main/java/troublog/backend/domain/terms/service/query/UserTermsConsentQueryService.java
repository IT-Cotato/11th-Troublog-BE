package troublog.backend.domain.terms.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.terms.repository.UserTermsConsentRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermsConsentQueryService {
	private final UserTermsConsentRepository userTermsConsentRepository;

	public List<UserTermsConsent> findAllByUserId(Long userId) {
		return userTermsConsentRepository.findAllByUserId(userId);
	}
}
