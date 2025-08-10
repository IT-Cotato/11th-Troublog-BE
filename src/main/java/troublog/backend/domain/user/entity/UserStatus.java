package troublog.backend.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
	ACTIVE("ACTIVE", "활성"),
	INCOMPLETE("INCOMPLETE", "추가 정보 필요");

	private final String key;
	private final String title;
}
