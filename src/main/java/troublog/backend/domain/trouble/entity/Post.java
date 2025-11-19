package troublog.backend.domain.trouble.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
import troublog.backend.domain.trouble.enums.TemplateType;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;
import troublog.backend.global.common.util.JsonConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "introduction")
	private String introduction;

	@Column(name = "thumbnail_url")
	private String thumbnailUrl;

    @NotNull
	@Column(name = "like_count", nullable = false)
    @Builder.Default
	private Integer likeCount = 0;

    @NotNull
	@Column(name = "comment_count", nullable = false)
    @Builder.Default
	private Integer commentCount = 0;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "template_type")
	private TemplateType templateType;

	@Column(name = "checklist_error", columnDefinition = "JSON")
	private String checklistError;

	@Column(name = "checklist_reason", columnDefinition = "JSON")
	private String checklistReason;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostSummary> postSummaries = new ArrayList<>();

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

	public void addPostSummary(PostSummary postSummary) {
		if (postSummary == null) {
			throw new PostException(ErrorCode.MISSING_POST_SUMMARY);
		}
		this.postSummaries.add(postSummary);
		postSummary.assignPost(this);
	}

	public void addComment(Comment comment) {
		if (comment == null) {
			return;
		}
		this.comments.add(comment);
		comment.assignPost(this);
		this.commentCount++;
	}

	public void removeComment(Comment comment) {
		if (comment == null) {
			return;
		}
		this.comments.remove(comment);
		this.commentCount = Math.max(0, this.commentCount - 1);
	}

	public void addLike(Like like) {
		if (like == null) {
			throw new PostException(ErrorCode.MISSING_LIKE);
		}
		likes.add(like);
		if (like.getPost() != this) {
			like.assignPost(this);
		}
		this.likeCount++;
	}

	public void removeLike(Like like) {
		if (like == null) {
			return;
		}
		likes.remove(like);
		this.likeCount = Math.max(0, this.likeCount - 1);
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
		if (status == PostStatus.SUMMARIZED) {
			return;
		}

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

	public void updateCommonInfo(PostReqDto postReqDto) {
		this.updateTitle(postReqDto.title());
		this.updateIntroduction(postReqDto.introduction());
		this.updateVisibility(postReqDto.isVisible());
		this.updateStatus(PostStatus.from(postReqDto.postStatus()));
		this.updateThumbnailUrl(postReqDto.thumbnailImageUrl());
		this.updateStarRating(StarRating.from(postReqDto.starRating()));
		this.checklistError = JsonConverter.toJson(postReqDto.checklistError());
		this.checklistReason = JsonConverter.toJson(postReqDto.checklistReason());
	}

	public void registerAsSummarized() {
		this.status = PostStatus.SUMMARIZED;
		this.isSummaryCreated = true;
	}
}
