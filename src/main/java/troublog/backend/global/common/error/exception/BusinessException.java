package troublog.backend.global.common.error.exception;

import lombok.Getter;
import troublog.backend.global.common.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}
