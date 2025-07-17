package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum Rating {
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5);

	private final int value;
}
