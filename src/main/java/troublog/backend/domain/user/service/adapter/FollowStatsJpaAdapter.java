package troublog.backend.domain.user.service.adapter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.repository.FollowRepository;
import troublog.backend.domain.user.repository.port.FollowStatsQueryPort;
import troublog.backend.domain.user.repository.value.FollowStats;
import troublog.backend.domain.user.repository.value.FollowStatsRow;

@Component
@RequiredArgsConstructor
class FollowStatsJpaAdapter implements FollowStatsQueryPort {

	private final FollowRepository followRepository;

	@Override
	public Map<Long, FollowStats> loadFor(Set<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyMap();
		}
		Map<Long, FollowStats> map = followRepository.findFollowStats(userIds).stream()
			.collect(Collectors.toMap(
				FollowStatsRow::getUserId,
				r -> FollowStats.builder()
					.followers(r.getFollowers())
					.followings(r.getFollowings())
					.build()
			));

		userIds.forEach(id -> map.putIfAbsent(id, FollowStats.zero()));
		return map;
	}
}