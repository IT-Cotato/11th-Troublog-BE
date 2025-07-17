package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum StarRating {
	ONE_STAR(1),
	TWO_STARS(2),
	THREE_STARS(3),
	FOUR_STARS(4),
	FIVE_STARS(5);

	private final int value;
}
