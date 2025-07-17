package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ContentSummaryType {
	RESUME("이력서"),
	INTERVIEW("면접"),
	BLOG("블로그"),
	ISSUE_MANAGEMENT("이슈관리");

	private final String description;
}
