package troublog.backend.domain.user.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.policy.entity.Policy;
import troublog.backend.domain.policy.exception.PolicyException;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_policy", indexes = {
	@Index(name = "idx_userpolicy_updated_at", columnList = "updated_at, is_agreed")
})
public class UserPolicy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "is_agreed")
	private Boolean isAgreed;

	@NotNull
	@Builder.Default
	@OneToMany(mappedBy = "userPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Policy> policies = new ArrayList<>();

	public void addPolicy(Policy policy) {
		if (Objects.isNull(policy)) {
			throw new PolicyException(ErrorCode.MISSING_POLICY);
		}
		this.policies.add(policy);
		policy.assignUserPolicy(this);
	}

}