package troublog.backend.domain.user.service.query;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.FollowRepository;
import troublog.backend.domain.user.repository.port.FollowStatsQueryPort;
import troublog.backend.domain.user.repository.value.FollowStats;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowQueryService {

	private final FollowRepository followRepository;
	private final FollowStatsQueryPort followStatsQueryPort;

	public void existsByFollowerAndFollowing(User follower, User following) {

		if (followRepository.existsByFollowerAndFollowing(follower, following)) {
			throw new UserException(ErrorCode.DUPLICATED_FOLLOWED);
		}
	}

	public Follow findByFollowerAndFollowing(User follower, User following) {

		return followRepository.findByFollowerAndFollowing(follower, following)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOLLOWED));
	}

	public List<User> findFollowers(User targetUser) {
		return followRepository.findFollowers(targetUser.getId());
	}

	public List<User> findFollowings(User user) {
		return followRepository.findFollowings(user.getId());
	}

	public Map<Long, FollowStats> loadFollowStats(Set<Long> userIds) {
		return followStatsQueryPort.loadFor(userIds); // Map<Long, FollowStats>
	}
}
