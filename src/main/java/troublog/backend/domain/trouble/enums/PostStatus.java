package troublog.backend.domain.trouble.enums;

import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum PostStatus {
	WRITING("작성 중"),
	WRITTEN("작성 완료"),
	SUMMARY_DONE("요약 완료"),
	;

	private final String message;
}
