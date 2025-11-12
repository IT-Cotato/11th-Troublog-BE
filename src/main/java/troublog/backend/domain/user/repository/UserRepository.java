package troublog.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailAndIsDeletedFalse(String email);

	boolean existsByEmailAndIsDeletedFalse(String email);

	boolean existsByNicknameAndIsDeletedFalse(String nickname);

	Optional<User> findBySocialIdAndIsDeletedFalse(String socialId);

	List<User> findAllByIdInAndIsDeletedFalse(Set<Long> userIds);
}