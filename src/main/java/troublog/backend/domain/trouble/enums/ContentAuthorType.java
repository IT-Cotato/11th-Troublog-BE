package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ContentAuthorType {
	USER_WRITTEN("사용자 작성"),
	AI_GENERATED("AI 생성");

	private final String description;

	public static ContentAuthorType from(String type) {
		for (ContentAuthorType contentSummaryType : values()) {
			if (contentSummaryType.description.equals(type)) {
				return contentSummaryType;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}
}
