package troublog.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResDto(

	@Schema(description = "유저아이디")
	Long userId,

	@Schema(description = "액세스토큰")
	@NotNull
	String accessToken,

	@Schema(description = "리프레시토큰")
	@NotNull
	String refreshToken,

	@Schema(description = "로컬작업용토큰")
	String localToken

) {
	public static LoginResDto localReturn(Long userId, String accessToken, String refreshToken, String localToken) {
		return LoginResDto.builder()
			.userId(userId)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.localToken(localToken)
			.build();
	}

	public static LoginResDto nonLocalReturn(Long userId, String accessToken, String refreshToken) {
		return LoginResDto.builder()
			.userId(userId)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
