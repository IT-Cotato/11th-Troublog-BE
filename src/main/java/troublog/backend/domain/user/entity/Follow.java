package troublog.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "follows",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_follow_follower_following",
		columnNames = {"follower_id", "following_id"}),
	indexes = {
		@Index(name = "idx_follow_following_created", columnList = "following_id, created_at DESC"),
		@Index(name = "idx_follow_follower_created", columnList = "follower_id, created_at DESC"),
		@Index(name = "idx_follow_stats_covering", columnList = "following_id, follower_id, created_at")
	})
public class Follow extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_id", nullable = false)
	private User follower;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_id", nullable = false)
	private User following;
}
