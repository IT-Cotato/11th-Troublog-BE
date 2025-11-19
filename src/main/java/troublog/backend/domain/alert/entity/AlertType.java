package troublog.backend.domain.alert.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AlertException;

@Getter
@AllArgsConstructor
public enum AlertType {

	// 알림 타입
	COMMENTS("comments", "댓글 알림"),
	LIKES("likes", "좋아요 알림"),
	TROUBLES("troubles", "트러블 슈팅 알림");

	private final String name;
	private final String description;

	public static AlertType fromString(String alertType) {
		for (AlertType type : AlertType.values()) {
			if (type.name.equalsIgnoreCase(alertType)) {
				return type;
			}
		}
		throw new AlertException(ErrorCode.ALERT_TYPE_INVALID);
	}
}
