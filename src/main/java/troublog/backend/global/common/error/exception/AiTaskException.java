package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class AiTaskException extends BusinessException {
	public AiTaskException(ErrorCode errorCode) {
		super(errorCode);
	}
}