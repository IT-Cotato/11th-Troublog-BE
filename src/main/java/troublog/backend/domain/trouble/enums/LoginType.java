package troublog.backend.domain.trouble.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType {
	NORMAL("NORMAL"),
	KAKAO("KAKAO");

	private final String value;
}
