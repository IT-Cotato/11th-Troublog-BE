package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SortType {
	LATEST("최신순"),
	IMPORTANT("중요도순");

	private final String message;

	public static SortType from(String status) {
		for (SortType sortType : values()) {
			if (sortType.name().equals(status)) {
				return sortType;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}
}