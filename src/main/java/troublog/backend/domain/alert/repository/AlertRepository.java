package troublog.backend.domain.alert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.entity.AlertType;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

	@Query("""
        select a
        from Alert a
        where a.user.id = :userId
          and a.isRead = false
          and (:alertType is null or a.alertType = :alertType)
        order by a.id desc
    """)
	List<Alert> findAllByUserIdAndAlertTypeAndIsReadFalse(Long userId, AlertType alertType);
}
