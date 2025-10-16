package troublog.backend.domain.terms.service.command;

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
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermsConsentCommandService {
	private final UserTermsConsentRepository userTermsConsentRepository;

	public List<UserTermsConsent> saveAll(List<UserTermsConsent> userTermsConsentList) {
		return userTermsConsentRepository.saveAll(userTermsConsentList);
	}
}
