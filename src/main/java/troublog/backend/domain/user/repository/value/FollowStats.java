package troublog.backend.domain.user.repository.value;

import lombok.Builder;

@Builder
public record FollowStats(Long followers, long followings) {
	public static FollowStats zero() {
		return new FollowStats(0L, 0);
	}
}