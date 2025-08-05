package troublog.backend.domain.ai.summary.service.command;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.repository.SummaryTaskRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryTaskCommandService {
	private final SummaryTaskRepository summaryTaskRepository;

	public SummaryTask save(SummaryTask summaryTask) {
		return summaryTaskRepository.save(summaryTask);
	}
}