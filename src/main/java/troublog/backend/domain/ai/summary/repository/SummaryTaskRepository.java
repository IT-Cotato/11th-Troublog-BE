package troublog.backend.domain.ai.summary.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.ai.summary.entity.SummaryTask;

@Repository
public interface SummaryTaskRepository extends CrudRepository<SummaryTask, String> {
}