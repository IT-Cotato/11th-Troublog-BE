package troublog.backend.domain.user.validator;

import org.springframework.stereotype.Component;

import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Component
public class UserValidator {

	public static void validateProfileUpdateRequest(Long userId, Long requestingUserId) {
		if (!userId.equals(requestingUserId)) {
			throw new UserException(ErrorCode.USER_UPDATE_SELF);
		}
	}

	public static void validateUserDeleted(User user) {
		if (user.getIsDeleted()) {
			throw new UserException(ErrorCode.USER_DELETED);
		}
	}
}
