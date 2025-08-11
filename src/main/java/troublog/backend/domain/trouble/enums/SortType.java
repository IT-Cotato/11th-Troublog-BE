package troublog.backend.domain.trouble.enums;

import java.util.Arrays;

import org.springframework.data.domain.Sort;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SortType {
	IMPORTANT("recommended", Sort.by(Sort.Direction.DESC, "like_count", "id")),
	LIKES("likes", Sort.by(Sort.Direction.DESC, "like_count", "id")),
	LATEST("latest", Sort.by(Sort.Direction.DESC, "created_at", "id"));

	private final String value;
	private final Sort sort;

	public static SortType from(String value) {
		return Arrays.stream(values())
			.filter(type -> type.value.equalsIgnoreCase(value))
			.findFirst()
			.orElse(LATEST);
	}
}