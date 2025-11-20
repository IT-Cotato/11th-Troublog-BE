package troublog.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailAndIsDeletedFalse(String email);

	boolean existsByNicknameAndIsDeletedFalse(String nickname);

	List<User> findAllByIdInAndIsDeletedFalse(Set<Long> userIds);

	boolean existsByEmailAndIsDeletedFalseAndStatus(String email, UserStatus userStatus);

	Optional<User> findByEmailAndIsDeletedFalseAndStatus(String email, UserStatus userStatus);
}