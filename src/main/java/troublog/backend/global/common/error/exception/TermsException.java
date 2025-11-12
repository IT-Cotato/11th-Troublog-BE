package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class TermsException extends BusinessException {

	public TermsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
