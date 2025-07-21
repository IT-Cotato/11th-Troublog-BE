package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.ErrorTag;

public interface ErrorTagRepository extends JpaRepository<ErrorTag, Long> {
}