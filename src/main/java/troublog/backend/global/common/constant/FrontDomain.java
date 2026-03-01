package troublog.backend.global.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AuthException;

@Getter
@RequiredArgsConstructor
public enum FrontDomain {

	LOCAL("http://localhost:5173"),
	DEV("https://www.troublog.com"),
	PROD("https:/www.troublog.com");

	private final String url;

	public static String fromEnvType(EnvType envType) {
		return switch (envType) {
			case LOCAL -> LOCAL.getUrl();
			case DEV -> DEV.getUrl();
			case PROD -> PROD.getUrl();
			default -> throw new AuthException(ErrorCode.WRONG_ENVIRONMENT);
		};
	}
}
