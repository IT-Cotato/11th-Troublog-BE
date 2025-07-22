package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum TagCategory {
	FRONTEND("프론트엔드 기술"),
	BACKEND("백엔드 기술"),
	DATABASE("데이터베이스 기술"),
	DEVOPS("데브옵스 기술"),
	INFRA("인프라 기술"),
	TOOL("개발 도구 및 공통 기술");

	private final String description;

	public static boolean isTechStackCategory(TagCategory tagCategory) {
		return tagCategory == FRONTEND || tagCategory == BACKEND || tagCategory == DATABASE ||
			tagCategory == DEVOPS || tagCategory == INFRA || tagCategory == TOOL;
	}
}
