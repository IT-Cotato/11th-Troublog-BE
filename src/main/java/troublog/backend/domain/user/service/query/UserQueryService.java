package troublog.backend.domain.user.service.query;

import static troublog.backend.domain.user.validator.UserValidator.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;
import troublog.backend.domain.user.repository.UserRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

	private final UserRepository userRepository;

	public User findUserById(Long userId) {

		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
	}

	public User findUserByIdAndStatusActive(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		validateUserDeleted(user);
		validateUserStatus(user);

		return user;
	}

	public User findUserByEmailAndStatusActive(String email) {

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		validateUserDeleted(user);
		validateUserStatus(user);

		return user;
	}

	public List<User> findAllByIds(Set<Long> userIds) {
		return userRepository.findAllByIdIn(userIds);
	}

	public boolean existsByEmail(String email) {

		return userRepository.existsByEmail(email);
	}

	public boolean existsByNickname(String nickname) {

		return userRepository.existsByNickname(nickname);
	}

	public Optional<User> findUserByEmailAndStatusActiveSocial(String email) {
		return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE);
	}
}
