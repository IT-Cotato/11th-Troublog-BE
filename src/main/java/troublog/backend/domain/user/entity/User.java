package troublog.backend.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import troublog.backend.domain.like.entity.Like;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.global.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

	@NotNull
	@Column(name = "nickname", unique = true)
	private String nickname;

	// TODO : 소셜로그인시 받아오는 이름?
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "field")
	private String field;

	@NotNull
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
}
