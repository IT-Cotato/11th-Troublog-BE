package troublog.backend.global.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AuthException;

@Getter
@RequiredArgsConstructor
public enum Domain {

	LOCAL("http://localhost:5173"),
	DEV("https://troublog.vercel.app")
	;

	private final String domain;

	public static String fromEnvType(EnvType envType) {
		return switch (envType) {
			case LOCAL -> LOCAL.getDomain();
			case DEV -> DEV.getDomain();
			default -> throw new AuthException(ErrorCode.WRONG_ENVIRONMENT);
		};
	}
}
