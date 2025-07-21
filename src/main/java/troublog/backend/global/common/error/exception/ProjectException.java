package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class ProjectException extends BusinessException {

	public ProjectException(ErrorCode errorCode) {
		super(errorCode);
	}
}