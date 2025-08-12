package troublog.backend.domain.user.repository.value;

public interface FollowStatsRow {
	Long getUserId();
	long getFollowers();
	long getFollowings();
}