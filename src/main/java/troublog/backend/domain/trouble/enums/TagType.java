package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum TagType {
	TECH_STACK("기술스택"),
	ERROR("에러태그");

	private final String description;

	public static boolean isErrorTag(TagType tagType) {
		return tagType == ERROR;
	}
}