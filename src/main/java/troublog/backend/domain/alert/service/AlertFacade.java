package troublog.backend.domain.alert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.converter.AlertConverter;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.entity.AlertType;
import troublog.backend.domain.alert.validator.AlertValidator;
import troublog.backend.global.common.util.AlertSseUtil;

@Service
@RequiredArgsConstructor
public class AlertFacade {

	private final AlertQueryService alertQueryService;
	private final AlertCommandService alertCommandService;
	private final AlertSseUtil alertSseUtil;

	@Transactional(readOnly = true)
	public List<AlertResDto> getAlerts(Long userId, String alertType) {

		AlertType type = (StringUtils.hasText(alertType))
			? AlertType.fromString(alertType)
			: null;

		return alertQueryService.getAlerts(userId, type).stream()
			.map(AlertConverter::convertToAlertResDto)
			.toList();
	}

	@Transactional
	public void deleteAlert(Long alertId, Long userId) {

		Alert alert = alertQueryService.getAlertById(alertId);

		// 자기자신의 알람인지 확인
		AlertValidator.validateUserAlarm(alert, userId);

		alertCommandService.deleteAlert(alert);
	}

	public SseEmitter connect(Long userId) {
		return alertSseUtil.connect(userId);
	}
}
