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
@Table(name = "alerts")
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
}
