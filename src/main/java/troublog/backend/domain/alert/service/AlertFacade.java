package troublog.backend.domain.alert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.converter.AlertConverter;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.entity.AlertType;
import troublog.backend.global.common.util.AlertSseUtil;

@Service
@RequiredArgsConstructor
public class AlertFacade {

	private final AlertQueryService alertQueryService;
	private final AlertCommandService alertCommandService;
	private final AlertSseUtil alertSseUtil;

	public List<AlertResDto> getAlerts(Long userId, String alertType) {

		AlertType type = (StringUtils.hasText(alertType))
			? AlertType.fromString(alertType)
			: null;

		return alertQueryService.getAlerts(userId, type).stream()
			.map(AlertConverter::convertToAlertResDto)
			.collect(Collectors.toList());
	}

	public void deleteAlert(Long alertId) {

		Alert alert = alertQueryService.getAlertById(alertId);

		alertCommandService.deleteAlert(alert);
	}

	public SseEmitter connect(Long userId) {
		return alertSseUtil.connect(userId);
	}
}
