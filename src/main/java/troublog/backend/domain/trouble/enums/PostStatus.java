package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum PostStatus {
	WRITING("작성 중"),
	COMPLETED("작성 완료"),
	SUMMARIZED("요약 완료");

	private final String message;
}
