package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SummaryType {
	NONE("없음"),
	RESUME("이력서"),
	INTERVIEW("면접"),
	MEMOIRS("회고록"),
	ISSUE_MANAGEMENT("이슈관리");

	private final String description;

	public static SummaryType from(final String type) {
		if (type == null) {
			return NONE;
		}

		for (SummaryType summaryType : values()) {
			if (summaryType.name().equals(type)) {
				return summaryType;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}

	public static String getName(final SummaryType summaryType) {
		return summaryType.name();
	}

	public static void validate(final SummaryType summaryType) {
		if (summaryType == null) {
			throw new PostException(ErrorCode.MISSING_SUMMARY_TYPE);
		}
	}
}
