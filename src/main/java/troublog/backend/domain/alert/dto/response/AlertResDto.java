package troublog.backend.domain.alert.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AlertResDto(
	@Schema(description = "알림 아이디")
	Long alertId,
	@Schema(description = "알림제목")
	String title,
	@Schema(description = "알림내용")
	String message,
	@Schema(description = "알림유형")
	String alertType,
	@Schema(description = "읽음여부")
	boolean isRead,
	@Schema(description = "화면이동url")
	String targetUrl,
	@Schema(description = "사용자아이디")
	Long userId
) {
	public static AlertResDto of(
		Long alertId, String title, String message, String alertType, boolean isRead, String targetUrl, Long userId
	) {
		return AlertResDto.builder()
			.alertId(alertId)
			.title(title)
			.message(message)
			.alertType(alertType)
			.isRead(isRead)
			.targetUrl(targetUrl)
			.userId(userId)
			.build();
	}
}
