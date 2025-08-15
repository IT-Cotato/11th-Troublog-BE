package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.PostSummary;

public interface PostSummaryRepository extends JpaRepository<PostSummary, Long> {
}