package troublog.backend.domain.terms.entity;

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
import troublog.backend.domain.terms.enums.TermsType;
import troublog.backend.domain.terms.exception.TermsException;
import troublog.backend.domain.user.entity.UserTerms;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "terms", indexes = {
	@Index(name = "idx_terms_title_body_version", columnList = "title, body, version")
})
public class Terms extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "terms_id")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "terms_type")
	private TermsType termsType;

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
	@JoinColumn(name = "user_terms_id")
	private UserTerms userTerms;

	public void assignUserTerms(UserTerms userTerms) {
		if (Objects.isNull(userTerms)) {
			throw new TermsException(ErrorCode.MISSING_USER_TERMS);
		}
		this.userTerms = userTerms;
	}
}
