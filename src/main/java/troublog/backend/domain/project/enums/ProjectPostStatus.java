package troublog.backend.domain.project.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ProjectPostStatus {
	COMPLETED("작성 완료"),
	SUMMARIZED("요약 완료");

	private final String message;

	public static troublog.backend.domain.project.enums.ProjectPostStatus from(String status) {
		for (troublog.backend.domain.project.enums.ProjectPostStatus postStatus : values()) {
			if (postStatus.name().equals(status)) {
				return postStatus;
			}
		}
		throw new PostException(ErrorCode.INVALID_VALUE);
	}
}
