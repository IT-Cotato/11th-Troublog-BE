package troublog.backend.global.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class SoftDeleteEntity extends BaseEntity {

	@Column(name = "deleted_at")
	protected LocalDateTime deletedAt;

	public boolean isDeleted() {
		return deletedAt != null;
	}
}
