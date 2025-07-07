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

	/**
	 * Returns the EnvType constant that matches the given string identifier.
	 *
	 * @param clientEnvType the string representation of the environment type
	 * @return the corresponding EnvType, or null if no match is found
	 */
	public static EnvType valueOfEnvType(String clientEnvType) {
		return Arrays.stream(values())
			.filter(et -> et.envType.equals(clientEnvType))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Determines whether two EnvType instances represent the same environment type.
	 *
	 * @param serverEnvType the first EnvType to compare
	 * @param clientEnvType the second EnvType to compare
	 * @return true if both EnvType instances are equal; false otherwise
	 */
	public static boolean isEqualEnvType(EnvType serverEnvType, EnvType clientEnvType) {
		return Objects.equals(serverEnvType, clientEnvType);
	}

	/**
	 * Checks if this environment type is LOCAL.
	 *
	 * @return true if the current instance represents the LOCAL environment; false otherwise
	 */
	public boolean isLocal() {
		return this == LOCAL;
	}

	/**
	 * Checks if this environment type is DEV.
	 *
	 * @return {@code true} if the current instance represents the DEV environment; {@code false} otherwise.
	 */
	public boolean isDev() {
		return this == DEV;
	}
}
