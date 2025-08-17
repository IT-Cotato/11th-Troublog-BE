package troublog.backend.domain.alert.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record AlertResDto(
	Long alertId,
	String title,
	String message,
	String alertType,
	boolean isRead,

	String targetUrl,
	Long userId
) {
}
