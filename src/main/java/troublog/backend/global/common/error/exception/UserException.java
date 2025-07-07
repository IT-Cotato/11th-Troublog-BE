package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class UserException extends BusinessException{

	/**
	 * Constructs a new UserException with the specified error code.
	 *
	 * @param errorCode the error code representing the specific user-related error
	 */
	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
