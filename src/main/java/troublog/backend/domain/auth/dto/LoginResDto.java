package troublog.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResDto {

	@Schema(description = "유저아이디")
	@JsonProperty("userId")
	private Long userId;

	@Schema(description = "액세스토큰")
	@JsonProperty("accessToken")
	@NotNull
	private String accessToken;

	@Schema(description = "리프레시토큰")
	@JsonProperty("refreshToken")
	@NotNull
	private String refreshToken;

	@Schema(description = "로컬작업용토큰")
	@JsonProperty("localToken")
	private String localToken;

	/**
	 * Creates a new {@code LoginResDto} instance with the specified user ID, access token, refresh token, and local token.
	 *
	 * @param userId the unique identifier of the user
	 * @param accessToken the access token issued upon successful login
	 * @param refreshToken the refresh token for obtaining new access tokens
	 * @param localToken the token used for local operations
	 * @return a {@code LoginResDto} populated with the provided values
	 */
	public static LoginResDto of(Long userId, String accessToken, String refreshToken, String localToken) {
		return LoginResDto.builder()
			.userId(userId)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.localToken(localToken)
			.build();
	}
}
