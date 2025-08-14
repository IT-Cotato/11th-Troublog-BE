package troublog.backend.domain.alert.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.entity.AlertType;
import troublog.backend.domain.user.entity.User;

@UtilityClass
public class AlertConverter {

	public AlertResDto convertToAlertResDto(Alert alert) {
		return AlertResDto.builder()
			.alertId(alert.getId())
			.userId(alert.getUser().getId())
			.title(alert.getTitle())
			.message(alert.getMessage())
			.alertType(alert.getAlertType().getDescription())
			.build();
	}

	public static Alert postTroubleshootingAlert(User user, int count) {
		return Alert.builder()
			.title("미완성 트러블슈팅 알림")
			.message(String.format("아직 해결되지 않은 트러블 슈팅이 %d개 있어요!", count))
			.alertType(AlertType.TROUBLES)
			.user(user)
			.build();
	}

	public static Alert postLikesAlert(User targetUser, String userLikesName) {
		return Alert.builder()
			.title("포스트 좋아요 알림")
			.message(String.format("%s 님이 나의 포스트에 좋아요를 눌렀습니다.", userLikesName))
			.alertType(AlertType.LIKES)
			.user(targetUser)
			.build();
	}

	public static Alert postCommentAlert(User targetUser, String userCommentName) {
		return Alert.builder()
			.title("포스트 댓글 알림")
			.message(String.format("%s 님이 포스트에 댓글을 남겼습니다.", userCommentName))
			.alertType(AlertType.COMMENTS)
			.user(targetUser)
			.build();
	}

	public static Alert postChildCommentAlert(User targetUser, String userChildCommentName) {
		return Alert.builder()
			.title("포스트 대댓글 알림")
			.message(String.format("%s 님이 나의 댓글에 답글을 남겼습니다.", userChildCommentName))
			.alertType(AlertType.COMMENTS)
			.user(targetUser)
			.build();
	}
}
