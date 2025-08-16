package troublog.backend.domain.user.service.query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.UserRepository;
import troublog.backend.domain.user.validator.UserValidator;
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

	public User findUserByEmail(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
	}

	public User findUserByIdAndIsDeletedFalseAndStatusActive(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		UserValidator.validateUserDeleted(user);
		UserValidator.validateUserStatus(user);

		return user;
	}

	public User findUserByEmailAndIsDeletedFalseAndStatusActive(String email) {

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		UserValidator.validateUserDeleted(user);
		UserValidator.validateUserStatus(user);

		return user;
	}

	public List<User> findAllByIds(Set<Long> userIds) {
		return userRepository.findAllByIdInAndIsDeletedFalse(userIds);
	}

	public boolean existsByEmail(String email) {

		return userRepository.existsByEmail(email);
	}

	public boolean existsByNickname(String nickname) {

		return userRepository.existsByNickname(nickname);
	}

	public Optional<User> findUserBySocialId(String socialId) {

	return userRepository.findBySocialId(socialId);
	}
}
