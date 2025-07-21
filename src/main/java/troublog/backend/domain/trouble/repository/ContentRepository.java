package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
}