package troublog.backend.domain.alert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.repository.AlertRepository;
import troublog.backend.domain.user.entity.User;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertCommandService {

	private final AlertRepository alertRepository;
	
	public void deleteAlert(Alert alert) {
		alertRepository.delete(alert);
	}
	
	public Alert save(Alert alert) {
		return alertRepository.save(alert);
	}
}
