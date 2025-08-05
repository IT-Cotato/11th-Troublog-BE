package troublog.backend.domain.ai.validator;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AiTaskException;

@UtilityClass
public class TaskValidator {

	public void validateTaskCanBeCancelled(SummaryTask task) {
		if (task.isCompleted()) {
			throw new AiTaskException(ErrorCode.TASK_ALREADY_COMPLETE);
		}
	}
}
