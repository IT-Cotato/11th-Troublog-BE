package troublog.backend.domain.user.validator;

import org.springframework.stereotype.Component;

import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Component
public class FollowValidator {

	public void validateNotSelfFollow(Long followerId, Long followingId) {
		if (followerId.equals(followingId)) {
			throw new UserException(ErrorCode.USER_FOLLOW_SELF);
		}
	}
}
