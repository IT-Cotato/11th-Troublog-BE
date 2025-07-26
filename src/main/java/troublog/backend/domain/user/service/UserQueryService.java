package troublog.backend.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.dto.FollowDto;
import troublog.backend.domain.user.entity.User;
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

	public User findUserByEmail(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
	}

	public boolean existsByEmail(String email) {

		return userRepository.existsByEmail(email);
	}

	public boolean existsByNickname(String nickname) {

		return userRepository.existsByNickname(nickname);
	}

	public FollowDto getFollows(Long followerId, Long followingId) {
		// 자기 자신 팔로우/언팔로우 불가
		if(followerId.equals(followingId)) {
			throw new UserException(ErrorCode.USER_FOLLOW_SELF);
		}

		User follower = findUserById(followerId);
		User following = findUserById(followingId);

		return new FollowDto(follower, following);
	}

}
