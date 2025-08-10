package troublog.backend.domain.ai.summary.converter;

import java.time.LocalDateTime;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.dto.response.TaskStartResDto;
import troublog.backend.domain.ai.summary.dto.response.TaskStatusResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;
import troublog.backend.global.common.util.IdGenerator;

@UtilityClass
public class SummaryTaskConverter {

	public SummaryTask from(Long postId) {
		return SummaryTask.builder()
			.id(IdGenerator.generate())
			.postId(postId)
			.status(SummaryStatus.PENDING)
			.startedAt(LocalDateTime.now())
			.build();
	}

	public TaskStartResDto toStartResponseDto(SummaryTask summaryTask, Long userId) {
		return TaskStartResDto.builder()
			.taskId(summaryTask.getId())
			.userId(userId)
			.status(summaryTask.getStatus())
			.message(summaryTask.getStatus().getMessage())
			.createdAt(summaryTask.getStartedAt())
			.build();
	}

	public TaskStatusResDto toStatusResponseDto(SummaryTask summaryTask, Long userId) {
		return TaskStatusResDto.builder()
			.taskId(summaryTask.getId())
			.userId(userId)
			.status(summaryTask.getStatus())
			.message(summaryTask.getStatus().getMessage())
			.progress(summaryTask.getProgress())
			.result(summaryTask.getResult())
			.createdAt(summaryTask.getStartedAt())
			.completedAt(summaryTask.getCompletedAt())
			.build();
	}

}