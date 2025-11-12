package troublog.backend.domain.user.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.FollowRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowQueryService {

	private final FollowRepository followRepository;

	public void existsByFollowerAndFollowing(User follower, User following) {

		if (followRepository.existsByFollowerAndFollowing(follower, following)) {
			throw new UserException(ErrorCode.DUPLICATED_FOLLOWED);
		}
	}

	public Follow findByFollowerAndFollowing(User follower, User following) {

		return followRepository.findByFollowerAndFollowing(follower, following)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOLLOWED));
	}

	public List<User> findFollowers(Long userId) {
		return followRepository.findFollowers(userId);
	}

	public List<User> findFollowings(Long userId) {
		return followRepository.findFollowings(userId);
	}

	public List<Follow> findAllByFollowerOrFollowing(User user) {
		return followRepository.findAllByFollowerOrFollowing(user);
	}
}
