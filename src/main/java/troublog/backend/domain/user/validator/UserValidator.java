package troublog.backend.domain.user.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Component
@RequiredArgsConstructor
public class UserValidator {

	private final PasswordEncoder passwordEncoder;

	public void validateProfileUpdateRequest(Long userId, Long requestingUserId) {
		if (!userId.equals(requestingUserId)) {
			throw new UserException(ErrorCode.USER_UPDATE_SELF);
		}
	}

	public static void validateUserDeleted(User user) {
		if (Boolean.TRUE.equals(user.getIsDeleted())) {
			throw new UserException(ErrorCode.USER_DELETED);
		}
	}

	public static void validateUserStatus(User user) {
		if (user.getStatus() != UserStatus.ACTIVE) {
			throw new UserException(ErrorCode.USER_STATUS_INVALID);
		}
	}

	public void validateUserPassword(User user, String password) {
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UserException(ErrorCode.INVALID_USER);
		}
	}
}
