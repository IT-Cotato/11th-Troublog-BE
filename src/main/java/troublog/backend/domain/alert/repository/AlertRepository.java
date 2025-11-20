package troublog.backend.domain.alert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.entity.AlertType;

public interface AlertRepository extends JpaRepository<Alert, Long> {

	@Query("""
		    select a
		    from Alert a
		    where a.user.id = :userId
		      and (:alertType is null or a.alertType = :alertType)
		      and a.isSent = true
		    order by a.id desc
		""")
	List<Alert> findAllByUserIdAndAlertType(Long userId, AlertType alertType);
}
