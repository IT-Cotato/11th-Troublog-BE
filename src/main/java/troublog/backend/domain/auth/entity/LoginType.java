package troublog.backend.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {
	NORMAL("NORMAL"),
	KAKAO("KAKAO");

	private final String value;
}
