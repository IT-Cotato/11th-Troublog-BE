package troublog.backend.domain.alert.validator;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AlertException;

@UtilityClass
public class AlertValidator {

	public static void validateUserAlarm(Alert alert, Long userId) {
		if (!alert.getUser().getId().equals(userId)) {
			throw new AlertException(ErrorCode.ALERT_SELF_DELETE_CHECK);
		}
	}
}
