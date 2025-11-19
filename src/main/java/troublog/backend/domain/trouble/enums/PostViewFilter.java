package troublog.backend.domain.trouble.enums;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostViewFilter {
	WRITING("작성중"),
	COMPLETED("작성완료");

	private final String message;

	public List<PostStatus> toPostStatuses() {
		return switch (this) {
			case WRITING -> List.of(PostStatus.WRITING);
			case COMPLETED -> List.of(PostStatus.COMPLETED, PostStatus.SUMMARIZED);
		};
	}

	public static PostViewFilter from(String status) {
		for (PostViewFilter type : values()) {
			if (type.name().equals(status)) {
				return type;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}
}
