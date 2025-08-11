package troublog.backend.domain.alert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.entity.AlertType;
import troublog.backend.domain.alert.repository.AlertRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AlertException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlertQueryService {

	private final AlertRepository alertRepository;

	public List<Alert> getAlerts(Long userId, AlertType alertType) {
		return alertRepository.findAllByUserIdAndAlertType(userId, alertType);
	}

	public Alert getAlertById(Long alertId) {

	return alertRepository.findById(alertId)
			.orElseThrow(() -> new AlertException(ErrorCode.ALERT_NOT_FOUND));
	}
}
