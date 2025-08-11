package troublog.backend.domain.trouble.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "posts",
	indexes = {
		@Index(
			name = "idx_posts_user_deleted_title",
			columnList = "user_id, is_deleted, title"
		),
		@Index(
			name = "idx_posts_user_id",
			columnList = "user_id"
		),
		@Index(
			name = "idx_posts_is_deleted",
			columnList = "is_deleted"
		)
	}
)
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@NotNull
	@Column(name = "title")
	private String title;

	@Column(name = "introduction")
	private String introduction;

	@Column(name = "thumbnail_url")
	private String thumbnailUrl;

	@Column(name = "like_count")
	private int likeCount;

	@Column(name = "comment_count")
	private int commentCount;

	@Column(name = "visible")
	private Boolean isVisible;

	@Column(name = "summary_created")
	private Boolean isSummaryCreated;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_Status")
	private PostStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "star_rating")
	private StarRating starRating;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Content> contents = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostTag> postTags = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Like> likes = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	// 연관관계 편의 메서드들
	public void assignUser(User user) {
		if (user == null) {
			throw new PostException(ErrorCode.MISSING_USER);
		}
		this.user = user;
	}

	public void assignProject(Project project) {
		if (project == null) {
			throw new PostException(ErrorCode.MISSING_PROJECT);
		}
		this.project = project;
	}

	public void addContent(Content content) {
		if (content == null) {
			throw new PostException(ErrorCode.MISSING_CONTENT);
		}
		this.contents.add(content);
		content.assignPost(this);
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
		comment.assignPost(this);
	}

	public void addPostTag(PostTag postTag) {
		if (postTag == null) {
			throw new PostException(ErrorCode.MISSING_POST_TAG);
		}
		if (this.postTags.contains(postTag)) {
			return;
		}
		this.postTags.add(postTag);
		postTag.assignPost(this);
	}

	public void addLike(Like like) {
		if (like == null)
			return;
		if (this.likes.contains(like))
			return;
		this.likes.add(like);
		likeCount++;
	}

	public void removeLike(Like like) {
		if (like == null)
			return;
		if (this.likes.remove(like) && likeCount > 0)
				likeCount--;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void updateVisibility(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void updateStatus(PostStatus status) {
		this.status = status;
		if (status == PostStatus.COMPLETED) {
			this.completedAt = LocalDateTime.now();
		}
	}

	public void updateStarRating(StarRating starRating) {
		this.starRating = starRating;
	}

	public void updateThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public void markAsDeleted() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}

	public void restoreFromDeleted() {
		this.isDeleted = false;
		this.deletedAt = null;
	}

}
