package troublog.backend.domain.report.validator;

import lombok.experimental.UtilityClass;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ReportException;

@UtilityClass
public class ReportValidator {

	public void validateReporterMatches(long authUserId, long reportingUserId) {
		if (authUserId != reportingUserId) {
			throw new ReportException(ErrorCode.REPORT_REPORTER_MISMATCH);
		}
	}

	public void validateReportedUserMatchesTarget(long reportedUserId, long targetOwnerId) {
		if (reportedUserId != targetOwnerId) {
			throw new ReportException(ErrorCode.REPORT_TARGET_USER_MISMATCH);
		}
	}

	public void validateDuplicate(boolean alreadyReported) {
		if (alreadyReported) {
			throw new ReportException(ErrorCode.REPORT_ALREADY_EXISTS);
		}
	}
}
