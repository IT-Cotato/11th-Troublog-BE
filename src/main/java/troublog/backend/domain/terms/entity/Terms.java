package troublog.backend.domain.terms.entity;

import java.time.LocalDateTime;

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
import troublog.backend.global.common.error.exception.TermsException;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "terms",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_terms_type_version",
			columnNames = {"terms_type", "version"})
	},
	indexes = {
		@Index(name = "idx_terms_current_only", columnList = "is_current, is_deleted"),
		@Index(name = "idx_terms_type_current", columnList = "terms_type, is_current"),
		@Index(name = "idx_terms_version", columnList = "version"),
		@Index(name = "idx_terms_is_deleted", columnList = "is_deleted")
	}
)
public class Terms extends BaseEntity {
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

	@Builder.Default
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "expiration_period")
	private Integer expirationPeriod;

	public void markAsDeleted() {
		if (isCurrent) {
			throw new TermsException(ErrorCode.CANNOT_DELETE_CURRENT_TERMS);
		}
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}

	public void markAsCurrent() {
		this.isCurrent = true;
	}

	public void markAsHistorical() {
		this.isCurrent = false;
	}
}
