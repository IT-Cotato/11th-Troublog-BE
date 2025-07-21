package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum PostStatus {
	WRITING("작성 중"),
	COMPLETED("작성 완료"),
	SUMMARIZED("요약 완료");

	private final String message;

	public static PostStatus from(String status) {
		for (PostStatus postStatus : values()) {
			if (postStatus.message.equals(status)) {
				return postStatus;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}
}
