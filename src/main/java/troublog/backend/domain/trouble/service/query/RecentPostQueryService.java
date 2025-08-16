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

	public List<Long> getRecentlyViewedPostIds(Long userId, long offset, int size) {
		String key = "recent_posts:" + userId;
		if (size <= 0)
			return List.of();

		long end = offset + size - 1;

		Set<Object> range = redisTemplate.opsForZSet().reverseRange(key, offset, end);
		if (range == null || range.isEmpty())
			return List.of();

		return range.stream()
			.map(id -> (id instanceof Number) ? ((Number)id).longValue()
				: Long.parseLong(id.toString()))
			.toList();
	}

	public long getRecentlyViewedCount(Long userId) {
		String key = "recent_posts:" + userId;
		Long cnt = redisTemplate.opsForZSet().zCard(key);
		return (cnt == null) ? 0L : cnt;
	}

}
