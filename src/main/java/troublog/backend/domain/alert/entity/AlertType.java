package troublog.backend.domain.alert.entity;

import lombok.Getter;

@Getter
public enum AlertType {

	// 알림 타입
	COMMENTS("댓글 알림"),
	LIKES("좋아요 알림"),
	TROUBLES("트러블 슈팅 알림");

	private final String description;

	AlertType(String description) {
		this.description = description;
	}
}
