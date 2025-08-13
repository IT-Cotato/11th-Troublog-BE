package troublog.backend.domain.ai.validator;

import java.util.Objects;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AiTaskException;

@UtilityClass
public class TaskValidator {

	public void validateTaskBelongsToPost(SummaryTask task, Long postId) {
		if (Objects.equals(task.getPostId(), postId)) {
			throw new AiTaskException(ErrorCode.TASK_POST_MISMATCH);
		}
	}

	public void validateTaskIsCompleted(SummaryTask task) {
		if (task.isCompleted()) {
			throw new AiTaskException(ErrorCode.TASK_ALREADY_COMPLETE);
		}
	}

	public boolean validateTaskStatusIsCompleted(SummaryTask task) {
		return task.getStatus() == SummaryStatus.COMPLETED;
	}
}
