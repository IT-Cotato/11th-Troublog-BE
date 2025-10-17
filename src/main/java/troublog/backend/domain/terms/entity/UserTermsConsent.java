package troublog.backend.domain.terms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.terms.enums.TermsType;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.entity.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user_terms_consent", indexes = {
	@Index(name = "idx_consent_user_terms",
		columnList = "user_id, terms_id"),
	@Index(name = "idx_consent_user_current",
		columnList = "user_id, is_current")
})
public class UserTermsConsent extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "terms_id", nullable = false)
	private Terms terms;

	@NotNull
	private Boolean isAgreed;

	@NotNull
	@Builder.Default
	private LocalDateTime agreedAt = LocalDateTime.now();

	@NotNull
	private Boolean isCurrent;

	// 쿼리 효율성을 위한 비정규화 필드
	@NotNull
	@Enumerated(EnumType.STRING)
	private TermsType termsType;

}
