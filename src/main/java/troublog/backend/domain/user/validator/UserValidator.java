package troublog.backend.domain.user.validator;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@UtilityClass
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

	public static void validateUserStatus(User user) {
		if(!user.getStatus().equals(UserStatus.ACTIVE)) {
			throw new UserException(ErrorCode.USER_STATUS_INVALID);
		}
	}
}
