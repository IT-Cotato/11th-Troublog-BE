package troublog.backend.domain.project.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ProjectException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "projects")
public class Project extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;

	@Column(name = "description")
	private String description;

	@Builder.Default
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();
	@NotNull
	@Column(name = "name")
	private String name;

	@Column(name = "thumbnail_image_url")
	private String thumbnailImageUrl;

	@Builder.Default
	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public void assignUser(User user) {
		if (user == null) {
			throw new ProjectException(ErrorCode.MISSING_USER);
		}
		this.user = user;
	}

	public void addPost(Post post) {
		if (post == null) {
			throw new ProjectException(ErrorCode.MISSING_POST);
		}
		if (this.posts.contains(post)) {
			return;
		}
		this.posts.add(post);
		post.assignProject(this);
	}

	public void update(String name, String description, String thumbnailImageUrl) {
		this.name = name;
		this.description = description;
		this.thumbnailImageUrl = thumbnailImageUrl;
	}

	public void softDelete() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}
}