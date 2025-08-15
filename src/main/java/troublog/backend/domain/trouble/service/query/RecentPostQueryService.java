package troublog.backend.domain.trouble.service.query;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentPostQueryService {

	private final RedisTemplate<String, Object> redisTemplate;

	public List<Long> getRecentlyViewedPostIds(Long userId, int limit) {
		String key = "recent_posts:" + userId;
		Set<Object> postIds = redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1);
		if (postIds == null)
			return List.of();
		return postIds.stream().map(id -> Long.valueOf(id.toString())).toList();
	}
}
