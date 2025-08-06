package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum VisibilityType {
	ALL("전체"),
	PUBLIC("공개"),
	PRIVATE("비공개");

	private final String message;

	public static VisibilityType from(String status) {
		for (VisibilityType visibilityType : values()) {
			if (visibilityType.name().equals(status)) {
				return visibilityType;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}
}
