package troublog.backend.global.common.constant;

import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AuthException;

@Getter
@RequiredArgsConstructor
public enum EnvType {

	LOCAL("local"),
	DEV("dev"),
	PROD("prod"),
	// 테스트는 임시로
	TEST("test");

	private final String value;

	public static EnvType valueOfEnvType(String inputEnvType) {
		return Arrays.stream(values())
			.filter(et -> et.value.equals(inputEnvType))
			.findFirst()
			.orElseThrow(() -> new AuthException(ErrorCode.WRONG_ENVIRONMENT));
	}

	public static boolean isEqualEnvType(EnvType serverEnvType, EnvType clientEnvType) {
		return Objects.equals(serverEnvType, clientEnvType);
	}

	public boolean isLocal() {
		return this == LOCAL;
	}

	public boolean isDev() {
		return this == DEV;
	}
}
