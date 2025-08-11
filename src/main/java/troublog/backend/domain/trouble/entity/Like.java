package troublog.backend.domain.trouble.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(
	name = "likes",
	uniqueConstraints = @jakarta.persistence.UniqueConstraint(
		name = "uk_likes_user_post",
		columnNames = {"user_id", "post_id"}
	)
)
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "likes_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@NotNull
	@Column(name = "liked_at", updatable = false)
	private LocalDateTime likedAt;

	@PrePersist
	protected void onCreate() {
		this.likedAt = LocalDateTime.now();
	}

	public static Like createLike(User user, Post post) {
		if (user == null)
			throw new PostException(ErrorCode.MISSING_USER);
		if (post == null)
			throw new PostException(ErrorCode.MISSING_POST);

		Like like = new Like();
		like.assignUser(user);
		like.assignPost(post);

		// 양방향 관계 세팅
		user.addLikeRef(like);
		post.addLike(like);
		return like;
	}

	public void assignUser(User user) {
		if (user == null) {
			throw new PostException(ErrorCode.MISSING_USER);
		}
		this.user = user;
	}

	public void assignPost(Post post) {
		if (post == null) {
			throw new PostException(ErrorCode.MISSING_POST);
		}
		this.post = post;
	}
}
