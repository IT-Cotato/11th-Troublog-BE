package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class AuthException extends BusinessException{

	public AuthException(ErrorCode errorCode) {
		super(errorCode);
	}
}
