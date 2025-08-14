package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class AlertException extends BusinessException{

	public AlertException(ErrorCode errorCode) {
		super(errorCode);
	}
}
