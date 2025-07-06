package troublog.backend.global.common.constant;

import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnvType {

	LOCAL("local"),
	DEV("dev"),
	PROD("prod"),
	// 테스트는 임시로
	TEST("test");

	private final String envType;

	public static EnvType valueOfEnvType(String clientEnvType) {
		return Arrays.stream(values())
			.filter(et -> et.envType.equals(clientEnvType))
			.findFirst()
			.orElse(null);
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
