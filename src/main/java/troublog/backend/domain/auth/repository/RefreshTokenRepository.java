package troublog.backend.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.auth.entity.RefreshToken;
import troublog.backend.domain.user.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	@Query("SELECT rt FROM RefreshToken rt WHERE rt.user = :user AND rt.isRevoked = false ORDER BY rt.expiredAt DESC")
	Optional<RefreshToken> findFirstByUserAndRevoked(User user, boolean isRevoked);
}
