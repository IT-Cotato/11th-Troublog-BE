package troublog.backend.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "field")
	private String field;

	@Column(name = "bio")
	private String bio;

	@Column(name = "githubUrl")
	private String githubUrl;

	public static User registerUser(RegisterDto registerDto, String encodedPassword) {
		return User.builder()
			.email(registerDto.getEmail())
			.password(encodedPassword)
			.nickname(registerDto.getNickname())
			.field(registerDto.getField())
			.bio(registerDto.getBio())
			.githubUrl(registerDto.getGithubUrl())
			.build();
	}
}
