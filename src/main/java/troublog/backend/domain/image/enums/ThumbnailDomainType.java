package troublog.backend.domain.image.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ThumbnailDomainType {
	POST("게시물"),
	PROJECT("프로젝트"),
	USER("사용자");

	private final String description;
}
