package troublog.backend.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.global.common.entity.BaseEntity;

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

	@NotNull
	@Column(name = "field")
	private String field;

	@NotNull
	@Column(name = "bio")
	private String bio;

	@Column(name = "githubUrl")
	private String githubUrl;

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Project> projects = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Post> posts = new ArrayList<>();

	public static User registerUser(RegisterDto registerDto, String encodedPassword) {
		return User.builder()
			.email(registerDto.email())
			.password(encodedPassword)
			.nickname(registerDto.nickname())
			.field(registerDto.field())
			.bio(registerDto.bio())
			.githubUrl(registerDto.githubUrl())
			.build();
	}

	public void addProject(Project project) {
		this.projects.add(project);
		project.assignUser(this);
	}

	public void addPost(Post post) {
		this.posts.add(post);
		post.assignUser(this);
	}
}
