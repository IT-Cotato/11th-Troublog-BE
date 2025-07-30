package troublog.backend.domain.user.validator;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Component
public class FollowValidator {

	public void validateNotSelfFollow(@NotNull Long followerId, @NotNull Long followingId) {
		if (followerId.equals(followingId)) {
			throw new UserException(ErrorCode.USER_FOLLOW_SELF);
		}
	}
}
