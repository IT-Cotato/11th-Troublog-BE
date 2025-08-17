package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum TemplateType {
	FREE_FORM("자유양식", "사용자가 자유롭게 작성할 수 있는 양식"),
	GUIDELINE("가이드라인 양식", "정해진 가이드라인을 따라 작성하는 양식");

	private final String displayName;
	private final String description;
}
