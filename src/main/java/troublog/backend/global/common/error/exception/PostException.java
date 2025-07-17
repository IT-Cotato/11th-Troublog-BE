package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class PostException extends BusinessException {
	public PostException(ErrorCode errorCode) {
		super(errorCode);
	}
}
