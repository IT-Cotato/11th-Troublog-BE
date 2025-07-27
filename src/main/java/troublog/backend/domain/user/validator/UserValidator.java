package troublog.backend.domain.user.validator;

import org.springframework.stereotype.Component;

import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Component
public class UserValidator {

	public void validateProfileUpdateRequest(Long userId, Long requestingUserId) {
		if (!userId.equals(requestingUserId)) {
			throw new UserException(ErrorCode.USER_UPDATE_SELF);
		}

	}
}
