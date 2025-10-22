package troublog.backend.domain.terms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import troublog.backend.domain.terms.entity.Terms;

public interface TermsRepository extends JpaRepository<Terms, Long> {
	@Query("SELECT T FROM Terms T WHERE T.isCurrent = TRUE AND T.isDeleted = FALSE")
	List<Terms> findActiveTerms();

}