package troublog.backend.domain.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.common.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
