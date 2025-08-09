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
import lombok.*;
import troublog.backend.domain.auth.dto.OAuth2RegisterReqDto;
import troublog.backend.domain.like.entity.Like;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.user.dto.request.UserProfileUpdateReqDto;
import troublog.backend.global.common.entity.BaseEntity;

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
	@Column(name = "email", unique = true)
	private String email;

	@NotNull
	@Column(name = "password")
	private String password;

	@Column(name = "nickname", unique = true)
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

	@Column(name = "login_type")
	private String loginType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private UserStatus status;

	@Column(name = "social_id")
	private String socialId;

	@NotNull
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	public void addProject(Project project) {
		this.projects.add(project);
		project.assignUser(this);
	}

	public void addPost(Post post) {
		this.posts.add(post);
		post.assignUser(this);
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

	public void updateOAuth2Info(OAuth2RegisterReqDto oAuth2RegisterReqDto) {
		this.nickname = oAuth2RegisterReqDto.nickname();
		this.bio = oAuth2RegisterReqDto.bio();
		this.field = oAuth2RegisterReqDto.field();
		this.githubUrl = oAuth2RegisterReqDto.githubUrl();
		this.status = UserStatus.ACTIVE;
	}
}
