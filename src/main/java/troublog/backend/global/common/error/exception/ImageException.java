package troublog.backend.global.common.error.exception;

import troublog.backend.global.common.error.ErrorCode;

public class ImageException extends BusinessException{
	public ImageException(ErrorCode errorCode) {
		super(errorCode);
	}
}
