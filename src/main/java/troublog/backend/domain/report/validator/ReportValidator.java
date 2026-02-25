package troublog.backend.domain.report.validator;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.report.enums.ReportType;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ReportException;

@UtilityClass
public class ReportValidator {

	public static void validateReportedUserMatchesTarget(long reportedUserId, long targetOwnerId) {
		if (reportedUserId != targetOwnerId) {
			throw new ReportException(ErrorCode.REPORT_TARGET_USER_MISMATCH);
		}
	}

	public static void validateDuplicate(boolean alreadyReported) {
		if (alreadyReported) {
			throw new ReportException(ErrorCode.REPORT_ALREADY_EXISTS);
		}
	}

	public static void validateCopyrightImgUrl(final ReportType reportType, final String copyrightImgUrl) {
		if (StringUtils.isBlank(copyrightImgUrl)) {
			return;
		}
		if (reportType != ReportType.COPYRIGHT) {
			throw new ReportException(ErrorCode.REPORT_COPYRIGHT_IMG);
		}
	}
}
