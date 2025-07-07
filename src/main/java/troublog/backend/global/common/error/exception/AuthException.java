package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class AuthException extends BusinessException{

	/**
	 * Constructs an AuthException with the specified error code.
	 *
	 * @param errorCode the error code representing the authentication error
	 */
	public AuthException(ErrorCode errorCode) {
		super(errorCode);
	}
}
