package troublog.backend.domain.auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class RefreshToken extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refreshToken_id")
	private Long id;

	@Column(name = "expired_at")
	private Date expiredAt;

	@Column(name = "is_revoked")
	private boolean isRevoked = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public static RefreshToken of(User user, Date ttl) {
		return RefreshToken.builder()
			.expiredAt(ttl)
			.user(user)
			.build();
	}

	// 리프레시 토큰 철회 (논리삭제) 메서드
	public void revokeRefreshToken() {
		this.isRevoked = true;
	}
}
