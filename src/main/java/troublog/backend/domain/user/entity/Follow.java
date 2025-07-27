package troublog.backend.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "follows",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_follow_follower_following",
		columnNames = {"follower_id", "following_id"}))
public class Follow extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private Long id;

	// TODO : 추후 팔로우 / 팔로잉 데이터 많아지면 인덱스 추가

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_id", nullable = false)
	private User follower;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_id", nullable = false)
	private User following;
}
