package troublog.backend.domain.policy.entity;

import java.util.Objects;

import jakarta.persistence.Column;
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
import lombok.NonNull;
import troublog.backend.domain.policy.enums.PolicyType;
import troublog.backend.domain.policy.exception.PolicyException;
import troublog.backend.domain.user.entity.UserPolicy;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "policy", indexes = {
	@Index(name = "idx_policy_title_body_version", columnList = "title, body, version")
})
public class Policy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "policy_id")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "policy_type")
	private PolicyType policyType;

	@NotNull
	@Column(name = "title")
	private String title;

	@NotNull
	@Column(name = "body")
	private String body;

	@NonNull
	@Column(name = "version")
	private Integer version = 1;

	@NotNull
	@Column(name = "is_required")
	private Boolean isRequired;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_policy_id")
	private UserPolicy userPolicy;

	public void assignUserPolicy(UserPolicy userPolicy) {
		if (Objects.isNull(userPolicy)) {
			throw new PolicyException(ErrorCode.MISSING_USER_POLICY);
		}
		this.userPolicy = userPolicy;
	}
}