package troublog.backend.domain.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.auth.entity.RefreshToken;
import troublog.backend.domain.auth.repository.RefreshTokenRepository;
import troublog.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenQueryService {

	private final RefreshTokenRepository refreshTokenRepository;

	public Optional<RefreshToken> findLatestTokenByUser(User user) {

		return refreshTokenRepository.findFirstByUserAndRevoked(user, false);
	}
}
