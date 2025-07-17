package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class TroubleException extends BusinessException {
	public TroubleException(ErrorCode errorCode) {
		super(errorCode);
	}
}
