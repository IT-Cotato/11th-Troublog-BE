package troublog.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LoginResDto(

	@Schema(description = "유저아이디")
	@JsonProperty("userId")
	Long userId,

	@Schema(description = "액세스토큰")
	@JsonProperty("accessToken")
	@NotNull
	String accessToken,

	@Schema(description = "리프레시토큰")
	@JsonProperty("refreshToken")
	@NotNull
	String refreshToken,

	@Schema(description = "로컬작업용토큰")
	@JsonProperty("localToken")
	String localToken

) {
	public static LoginResDto of(Long userId, String accessToken, String refreshToken, String localToken) {
		return new LoginResDto(userId, accessToken, refreshToken, localToken);
	}
}
