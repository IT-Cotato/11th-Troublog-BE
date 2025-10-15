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
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.exception.TermsException;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_terms", indexes = {
	@Index(name = "idx_userterms_updated_at", columnList = "updated_at, is_agreed")
})
public class UserTerms extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(name = "is_agreed")
	private Boolean isAgreed;

	@NotNull
	@Builder.Default
	@OneToMany(mappedBy = "userTerms", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Terms> termsList = new ArrayList<>();

	// TODO: User 엔티티와의 연관관계 추가 필요

	public void addTerms(Terms terms) {
		if (Objects.isNull(terms)) {
			throw new TermsException(ErrorCode.MISSING_TERMS);
		}
		this.termsList.add(terms);
		terms.assignUserTerms(this);
	}

}
