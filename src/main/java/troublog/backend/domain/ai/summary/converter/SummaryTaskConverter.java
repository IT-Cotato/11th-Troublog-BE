package troublog.backend.domain.ai.summary.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.dto.response.TaskStartResDto;
import troublog.backend.domain.ai.summary.dto.response.TaskStatusResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;
import troublog.backend.domain.ai.validator.TaskValidator;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.global.common.util.IdGenerator;

@UtilityClass
public class SummaryTaskConverter {

	public SummaryTask from(Long postId, Long userId, SummaryType summaryType) {
		return SummaryTask.builder()
			.id(IdGenerator.generate())
			.postId(postId)
			.userId(userId)
			.status(SummaryStatus.PENDING)
			.summaryType(summaryType)
			.build();
	}

	public TaskStartResDto toStartResponseDto(SummaryTask summaryTask, Long userId) {
		return TaskStartResDto.builder()
			.taskId(summaryTask.getId())
			.userId(userId)
			.status(summaryTask.getStatus())
			.currentStep(summaryTask.getStatus().getMessage())
			.createdAt(summaryTask.getStartedAt())
			.build();
	}

	public TaskStatusResDto toStatusResponseDto(SummaryTask summaryTask, Long userId) {
		return TaskStatusResDto.builder()
			.taskId(summaryTask.getId())
			.userId(userId)
			.status(summaryTask.getStatus())
			.currentStep(summaryTask.getCurrentStep())
			.progress(summaryTask.getProgress())
			.result(TaskValidator.validateTaskStatusIsCompleted(summaryTask) ? summaryTask.getResult() : null)
			.createdAt(summaryTask.getStartedAt())
			.completedAt(summaryTask.getCompletedAt())
			.build();
	}

}