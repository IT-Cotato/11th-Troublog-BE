package troublog.backend.domain.ai.summary.service.facade;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.converter.SummaryTaskConverter;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;
import troublog.backend.domain.ai.summary.service.command.SummaryTaskCommandService;
import troublog.backend.domain.ai.summary.service.query.SummaryTaskQueryService;
import troublog.backend.domain.ai.validator.TaskValidator;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AiTaskException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryTaskFacade {

	private final SummaryTaskCommandService summaryTaskCommandService;
	private final SummaryTaskQueryService summaryTaskQueryService;

	public SummaryTask createTask(Long postId) {
		SummaryTask summaryTask = SummaryTaskConverter.from(postId);
		return summaryTaskCommandService.save(summaryTask);
	}

	public void updateTask(String taskId, SummaryStatus status) {
		SummaryTask task = summaryTaskQueryService.findTask(taskId);
		updateTaskStatus(task, status);
		summaryTaskCommandService.save(task);
	}

	public void cancelTask(String taskId) {
		SummaryTask task = summaryTaskQueryService.findTask(taskId);
		TaskValidator.validateTaskCanBeCancelled(task);
		updateTaskStatus(task, SummaryStatus.CANCELLED);
		summaryTaskCommandService.save(task);
		log.info("작업 취소: taskId={}, status={}, progress={}%", taskId, task.getStatus(), task.getProgress());
	}

	private void updateTaskStatus(SummaryTask task, SummaryStatus status) {
		switch (status) {
			case STARTED, PREPROCESSING, ANALYZING, POSTPROCESSING -> task.updateWorkingOnStatus(status);
			case COMPLETED, CANCELLED, FAILED -> task.updateCompletedStatus(status);
			default -> throw new AiTaskException(ErrorCode.TASK_UPDATE_FAILED);
		}
	}
}