package troublog.backend.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.user.dto.request.UserProfileUpdateReqDto;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@NotNull
	@Column(name = "email")
	private String email;

	@NotNull
	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "field")
	private String field;

	@Column(name = "bio")
	private String bio;

	@Column(name = "profile_url")
	private String profileUrl;

	@Column(name = "github_url")
	private String githubUrl;

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Project> projects = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Post> posts = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Like> likes = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Comment> comments = new ArrayList<>();

	@Column(name = "login_type")
	private String loginType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	@Builder.Default
	private UserStatus status = UserStatus.INCOMPLETE;

	@Column(name = "social_id")
	private String socialId;

	@NotNull
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@NotNull
	@Builder.Default
	@Column(name = "is_integrated")
	private Boolean isIntegrated = false;

	public void addProject(Project project) {
		this.projects.add(project);
		project.assignUser(this);
	}

	public void addPost(Post post) {
		this.posts.add(post);
		post.assignUser(this);
	}

	public void addComment(Comment comment) {
		if (comment == null)
			return;
		this.comments.add(comment);
		comment.assignUser(this);
	}

	public void removeComment(Comment comment) {
		if (comment == null)
			return;
		comments.remove(comment);
		if (comment.getUser() == this) {
			comment.assignUser(null);
		}
	}

	public void addLikeRef(Like like) {
		if (like == null) {
			throw new PostException(ErrorCode.MISSING_LIKE);
		}
		if (!likes.contains(like)) {
			likes.add(like);
			if (like.getUser() != this) {
				like.assignUser(this);
			}
		}
	}

	public void removeLikeRef(Like like) {
		if (like == null)
			return;
		if (likes.contains(like)) {
			like.unassignUser();
		}
	}

	public void updateUserProfile(UserProfileUpdateReqDto userProfileUpdateReqDto) {
		this.nickname = userProfileUpdateReqDto.nickname();
		this.field = userProfileUpdateReqDto.field();
		this.bio = userProfileUpdateReqDto.bio();
		this.githubUrl = userProfileUpdateReqDto.githubUrl();
		this.profileUrl = userProfileUpdateReqDto.profileUrl();
	}

	public void deleteUser() {
		this.isDeleted = true;
	}

	public void updateOAuth2Info(String nickname, String field, String bio, String githubUrl) {
		this.nickname = nickname;
		this.bio = bio;
		this.field = field;
		this.githubUrl = githubUrl;
		this.status = UserStatus.ACTIVE;
	}

	public void updateIntegrateKakaoUser(String password, String socialId, String profileUrl) {
		this.isIntegrated = true;
		this.password = password;
		this.socialId = socialId;
		this.profileUrl = profileUrl;
	}
}
