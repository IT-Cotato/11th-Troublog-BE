package troublog.backend.domain.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByNickname(String nickname);

	List<User> findAllByIdIn(Set<Long> userIds);

	Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

	boolean existsByEmail(String email);

	@Modifying
	@Query(
		value = """
			DELETE FROM users
			WHERE deleted_at IS NOT NULL
				AND deleted_at <= :threshold
			""",
		nativeQuery = true
	)
	int deleteAllSoftDeletedBefore(@Param("threshold") LocalDateTime threshold);
}
