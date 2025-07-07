package troublog.backend.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/**
 * Retrieves a user entity by its email address.
 *
 * @param email the email address to search for
 * @return an {@code Optional} containing the user if found, or empty if no user exists with the given email
 */
Optional<User> findByEmail(String email);
}