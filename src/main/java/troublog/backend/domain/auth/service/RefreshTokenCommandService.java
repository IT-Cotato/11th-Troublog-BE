package troublog.backend.domain.auth.service;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.auth.entity.RefreshToken;
import troublog.backend.domain.auth.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenCommandService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken save(RefreshToken refreshToken) {
		return refreshTokenRepository.save(refreshToken);
	}
}
