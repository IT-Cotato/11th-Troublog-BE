package troublog.backend.domain.trouble.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ContentAuthorType {
	USER_WRITTEN("사용자 작성"),
	AI_GENERATED("AI 생성");

	private final String description;
}
