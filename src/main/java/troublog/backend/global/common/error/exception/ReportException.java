package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class ReportException extends BusinessException {
	public ReportException(ErrorCode errorCode) {
		super(errorCode);
	}
}
