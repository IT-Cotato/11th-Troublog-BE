package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ContentSummaryType {
	NONE("없음"),
	RESUME("이력서"),
	INTERVIEW("면접"),
	BLOG("블로그"),
	ISSUE_MANAGEMENT("이슈관리");

	private final String description;

	public static ContentSummaryType from(String type) {
		if (type == null) {
			return NONE;
		}

		for (ContentSummaryType contentSummaryType : values()) {
			if (contentSummaryType.name().equals(type)) {
				return contentSummaryType;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}
}
