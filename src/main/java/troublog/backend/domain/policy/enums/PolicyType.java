package troublog.backend.domain.policy.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum PolicyType {

	TERMS_OF_USE("이용약관"),
	PRIVACY_POLICY("개인정보처리방침");

	private final String description;
}