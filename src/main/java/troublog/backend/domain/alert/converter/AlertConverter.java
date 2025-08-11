package troublog.backend.domain.alert.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;

@UtilityClass
public class AlertConverter {

	public AlertResDto convertToAlertResDto(Alert alert) {
		return AlertResDto.builder()
			.alertId(alert.getId())
			.userId(alert.getUser().getId())
			.title(alert.getTitle())
			.message(alert.getMessage())
			.alertType(alert.getAlertType().getDescription())
			.build();
	}
}
