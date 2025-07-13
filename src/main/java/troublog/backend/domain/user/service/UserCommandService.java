package troublog.backend.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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
