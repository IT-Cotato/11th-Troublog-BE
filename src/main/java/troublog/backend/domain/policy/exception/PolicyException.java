package troublog.backend.domain.policy.exception;

import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.BusinessException;

public class PolicyException extends BusinessException {

	public PolicyException(ErrorCode errorCode) {
		super(errorCode);
	}
}