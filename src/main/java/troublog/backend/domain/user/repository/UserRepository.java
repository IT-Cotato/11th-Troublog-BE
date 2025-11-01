package troublog.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndIsDeletedFalse(String email);

	boolean existsByEmailAndIsDeletedFalse(String email);

	boolean existsByNicknameAndIsDeletedFalse(String nickname);

	Optional<User> findBySocialIdAndIsDeletedFalse(String socialId);

	Optional<User> findBySocialId(String socialId);

	List<User> findAllByIdInAndIsDeletedFalse(Set<Long> userIds);
}