package troublog.backend.domain.trouble.service.facade.command;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentPostCommandFacade {

	private static final Duration TTL = Duration.ofDays(7);
	private final RedisTemplate<String, Object> redisTemplate;

	public void recordPostView(Long userId, Long postId) {
		String key = "recent_posts:" + userId;
		long now = System.currentTimeMillis();

		// 같은 postId는 중복 없이 score(now)만 갱신
		redisTemplate.opsForZSet().add(key, postId, now);

		// 마지막 열람 시점 기준으로 7일 유지
		redisTemplate.expire(key, TTL);
	}

}
