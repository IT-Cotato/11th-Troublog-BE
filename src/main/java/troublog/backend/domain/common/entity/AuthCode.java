package troublog.backend.domain.common.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "auth_code")
public class AuthCode extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("인증코드아이디")
	@Column(name = "auth_code_id")
	private Long authCodeId;

	@NotNull
	@Comment("인증코드")
	@Column(name = "auth_code")
	private String authCode;

	@NotNull
	@Comment("만료일시")
	@Column(name = "expire_date")
	private LocalDateTime expireDate;

	@NotNull
	@Comment("인증여부")
	@Column(name = "isAuth")
	private boolean isAuth;

	@NotNull
	@Comment("임의문자열")
	@Column(name = "random_string")
	private UUID randomString;

	public void updateIsAuth() {
		this.isAuth = true;
	}
}
