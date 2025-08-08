package troublog.backend.domain.ai.summary.service.facade;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.converter.SummaryTaskConverter;
import troublog.backend.domain.ai.summary.dto.TaskStatusResDto;
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

	public TaskStatusResDto findTask(String taskId, Long userId, Long postId) {
		SummaryTask summaryTask = summaryTaskQueryService.findTask(taskId);
		TaskValidator.validateTaskBelongsToPost(summaryTask, postId);
		return SummaryTaskConverter.toStatusResponseDto(summaryTask, userId);
	}

	public void updateTask(String taskId, SummaryStatus status) {
		SummaryTask task = summaryTaskQueryService.findTask(taskId);
		updateTaskStatus(task, status);
		summaryTaskCommandService.save(task);
	}

	public void cancelTask(String taskId, Long postId) {
		SummaryTask summaryTask = summaryTaskQueryService.findTask(taskId);

		TaskValidator.validateTaskBelongsToPost(summaryTask, postId);
		TaskValidator.validateTaskCanBeCancelled(summaryTask);

		updateTaskStatus(summaryTask, SummaryStatus.CANCELLED);
		summaryTaskCommandService.save(summaryTask);
		logTaskCancellation(summaryTask);
	}

	private void updateTaskStatus(SummaryTask task, SummaryStatus status) {
		switch (status) {
			case STARTED, PREPROCESSING, ANALYZING, POSTPROCESSING -> task.updateWorkingOnStatus(status);
			case COMPLETED, CANCELLED, FAILED -> task.updateCompletedStatus(status);
			default -> throw new AiTaskException(ErrorCode.TASK_UPDATE_FAILED);
		}
	}

	private void logTaskCancellation(SummaryTask summaryTask) {
		log.info("작업 취소: taskId={}, status={}, progress={}%", summaryTask.getId(), summaryTask.getStatus(),
			summaryTask.getProgress());
	}

	public void startSummaryTask(SummaryTask task) {

	}
}