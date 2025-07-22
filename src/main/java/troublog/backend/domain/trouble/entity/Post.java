package troublog.backend.domain.trouble.entity;

import java.time.LocalDateTime;
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
import troublog.backend.domain.image.entity.PostImage;
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
@Table(name = "posts")
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

	@Column(name = "like_count")
	private int likeCount;

	@Column(name = "comment_count")
	private int commentCount;

	@Column(name = "visible")
	private boolean isVisible;

	@Column(name = "summary_created")
	private boolean isSummaryCreated;

	@Enumerated(EnumType.STRING)
	@Column(name = "postStatus")
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

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Content> contents;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostTag> postTags;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostImage> postImages;

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

	public void addContents(List<Content> contents) {
		if (contents == null || contents.isEmpty()) {
			throw new PostException(ErrorCode.MISSING_CONTENT_LIST);
		}
		contents.forEach(this::addContent);
	}

	public void addContent(Content content) {
		if (content == null) {
			throw new PostException(ErrorCode.MISSING_CONTENT);
		}
		content.assignPost(this);
	}

	public void addPostTags(List<PostTag> postTags) {
		if (postTags == null || postTags.isEmpty()) {
			throw new PostException(ErrorCode.MISSING_POST_TAG_LIST);
		}
		postTags.forEach(this::addPostTag);
	}

	public void addPostTag(PostTag postTag) {
		if (postTag == null) {
			throw new PostException(ErrorCode.MISSING_POST_TAG);
		}
		postTag.assignPost(this);
	}

	public void addPostImages(List<PostImage> postImages) {
		if (postImages == null || postImages.isEmpty()) {
			throw new PostException(ErrorCode.MISSING_IMAGE_LIST);
		}
		postImages.forEach(this::addPostImage);
	}

	public void addPostImage(PostImage postImage) {
		if (postImage == null) {
			throw new PostException(ErrorCode.MISSING_IMAGE);
		}
		postImage.assignPost(this);
	}
}
