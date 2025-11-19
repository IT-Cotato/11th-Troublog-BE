package troublog.backend.domain.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.common.entity.AuthCode;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
	Optional<AuthCode> findByAuthCodeAndIsAuthFalse(String authCode);
}
