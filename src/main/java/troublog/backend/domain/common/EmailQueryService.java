package troublog.backend.domain.common;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.common.entity.AuthCode;
import troublog.backend.domain.common.repository.AuthCodeRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AuthException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailQueryService {

	private final AuthCodeRepository authCodeRepository;

	public AuthCode getAuthCode(String authCode) {
		return authCodeRepository.findByAuthCodeAndIsAuthFalse(authCode)
			.orElseThrow(() -> new AuthException(ErrorCode.AUTH_CODE_EMPTY));
	}

	public AuthCode getAuthCodeWithoutAuth(String authCode) {
		return authCodeRepository.findByAuthCode(authCode)
			.orElseThrow(() -> new AuthException(ErrorCode.AUTH_CODE_EMPTY));

	}
}
