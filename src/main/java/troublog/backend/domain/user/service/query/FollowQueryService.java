package troublog.backend.domain.user.service.query;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.FollowRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowQueryService {

	public static final int USER_ID_IDX = 0;
	public static final int FOLLOWING_COUNT_IDX = 1;
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

	public List<User> findFollowers(User targetUser) {
		return followRepository.findFollowers(targetUser.getId());
	}

	public List<User> findFollowings(User user) {
		return followRepository.findFollowings(user.getId());
	}

	public Map<Long, Long> countFollowersByUserIds(Set<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyMap();
		}

		return followRepository.countFollowersByUserIds(userIds)
			.stream()
			.collect(Collectors.toMap(
				result -> result[USER_ID_IDX],
				result -> result[FOLLOWING_COUNT_IDX],
				Long::sum
			));
	}

	public Map<Long, Long> countFollowingsByUserIds(Set<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyMap();
		}

		return followRepository.countFollowingsByUserIds(userIds)
			.stream()
			.collect(Collectors.toMap(
				result -> result[USER_ID_IDX],
				result -> result[FOLLOWING_COUNT_IDX],
				Long::sum
			));
	}
}
