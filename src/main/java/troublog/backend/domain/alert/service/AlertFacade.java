package troublog.backend.domain.alert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.converter.AlertConverter;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.entity.AlertType;

@Service
@RequiredArgsConstructor
public class AlertFacade {

	private final AlertQueryService alertQueryService;

	public List<AlertResDto> getAlerts(Long userId, String alertType) {

		AlertType type = (StringUtils.hasText(alertType))
			? AlertType.valueOf(alertType)
			: null;

		return alertQueryService.getUnreadAlerts(userId, type).stream()
			.map(AlertConverter::convertToAlertResDto)
			.collect(Collectors.toList());
	}

	public void deleteAlert(Long alertId) {

		Alert alert = alertQueryService.getAlertById(alertId);

		alert.markAsRead();
	}
}
