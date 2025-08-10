package troublog.backend.domain.ai.summary.service.query;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.repository.SummaryTaskRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AiTaskException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryTaskQueryService {
	private final SummaryTaskRepository summaryTaskRepository;

	public SummaryTask findTask(String taskId) {
		return summaryTaskRepository.findById(taskId)
			.orElseThrow(() -> new AiTaskException(ErrorCode.TASK_NOT_FOUND));
	}
}