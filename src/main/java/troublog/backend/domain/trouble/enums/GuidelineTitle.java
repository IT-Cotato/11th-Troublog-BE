package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum GuidelineTitle {
	GUIDELINE_1("어떤 오류가 발생했나요?", "발생한 오류"),
	GUIDELINE_2("문제 원인을 파악해보세요!", "문제 원인 파악"),
	GUIDELINE_3("어떤 방법으로 해결하고 적용했나요?", "해결 방법 및 적용"),
	GUIDELINE_4("검증과 회고를 작성해보세요!", "검증 및 회고");

	private final String originalTitle;
	private final String simplifiedTitle;

	public static String getSimplifiedTitleFromSequence(final Integer sequence) {
		validateIndex(sequence);
		return values()[sequence].getSimplifiedTitle();
	}

	private static void validateIndex(final Integer index) {
		if (index == null) {
			throw new PostException(ErrorCode.SEQUENCE_NOT_VALID);
		}
		if (index < 0 || index >= values().length) {
			throw new PostException(ErrorCode.SEQUENCE_NOT_VALID);
		}
	}
}