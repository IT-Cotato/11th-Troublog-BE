package troublog.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByNickname(String nickname);

	List<User> findAllByIdIn(Set<Long> userIds);

	Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

	boolean existsByEmail(String email);
}
