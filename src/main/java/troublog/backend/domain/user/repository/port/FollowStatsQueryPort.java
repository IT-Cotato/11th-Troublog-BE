package troublog.backend.domain.user.repository.port;

import java.util.Map;
import java.util.Set;

import troublog.backend.domain.user.repository.value.FollowStats;

public interface FollowStatsQueryPort {
	Map<Long, FollowStats> loadFor(Set<Long> userIds);
}