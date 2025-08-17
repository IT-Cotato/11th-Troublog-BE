package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.SummaryContent;

public interface SummaryContentRepository extends JpaRepository<SummaryContent, Long> {
	List<SummaryContent> findAllByPostSummary_Id(Long postSummaryId);
}