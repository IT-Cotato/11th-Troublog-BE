package troublog.backend.domain.user.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserCommandService {

	private final UserRepository userRepository;

	@Transactional
	public Long save(User user) {
		return userRepository.save(user).getId();
	}
}
