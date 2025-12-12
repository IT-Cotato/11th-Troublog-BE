package troublog.backend.domain.terms.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.terms.enums.TermsType;
import troublog.backend.global.common.entity.SoftDeleteEntity;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE terms SET deleted_at = current_timestamp WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "terms",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_terms_type_version",
			columnNames = {"terms_type", "version"})
	},
	indexes = {
		@Index(name = "idx_terms_current_only", columnList = "is_current, deleted_at"),
		@Index(name = "idx_terms_type_current", columnList = "terms_type, is_current"),
		@Index(name = "idx_terms_version", columnList = "version"),
		@Index(name = "idx_terms_deleted_at", columnList = "deleted_at")
	}
)
public class Terms extends SoftDeleteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "terms_type")
	private TermsType termsType;

	@NotNull
	private String title;

	@NotNull
	@Column(columnDefinition = "TEXT")
	private String body;

	@NotNull
	private Integer version;

	@NotNull
	private Boolean isRequired;

	@NotNull
	@Builder.Default
	@Column(name = "is_current")
	private Boolean isCurrent = false;

	@Column(name = "expiration_period")
	private Integer expirationPeriod;
}
