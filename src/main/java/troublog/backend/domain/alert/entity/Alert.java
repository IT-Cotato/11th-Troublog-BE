package troublog.backend.domain.alert.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "alert")
public class Alert extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alert_id")
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "message")
	private String message;

	@Column(name = "is_sent")
	@Builder.Default
	private boolean isSent = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "alert_type")
	private AlertType alertType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public void markAsSent() {
		this.isSent = true;
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
