package troublog.backend.domain.terms.exception;

import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.BusinessException;

public class TermsException extends BusinessException {

	public TermsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
