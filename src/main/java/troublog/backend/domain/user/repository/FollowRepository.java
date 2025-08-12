package troublog.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.value.FollowStatsRow;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	boolean existsByFollowerAndFollowing(User follower, User following);

	Optional<Follow> findByFollowerAndFollowing(User follower, User following);

	@Query("""
		    SELECT f.following FROM Follow f
		    WHERE f.follower.id = :targetUserId
		    ORDER BY f.following.id DESC
		""")
	List<User> findFollowers(@Param("targetUserId") Long targetUserId);

	// TODO : 추후 페이징 적용하기
	@Query("""
		    SELECT f.follower FROM Follow f
		    WHERE f.following.id = :userId
		    ORDER BY f.follower.id DESC
		""")
	List<User> findFollowings(@Param("userId") Long userId);

	@Query(value = """
		SELECT user_id,
		       SUM(follower_cnt)  AS followers,
		       SUM(following_cnt) AS followings
		FROM (
		    SELECT f.following_id AS user_id, COUNT(*) AS follower_cnt, 0 AS following_cnt
		    FROM follows f
		    WHERE f.following_id IN (:userIds)
		    GROUP BY f.following_id
		    UNION ALL
		    SELECT f.follower_id  AS user_id, 0 AS follower_cnt, COUNT(*) AS following_cnt
		    FROM follows f
		    WHERE f.follower_id IN (:userIds)
		    GROUP BY f.follower_id
		) t
		GROUP BY user_id
		""", nativeQuery = true)
	List<FollowStatsRow> findFollowStats(@Param("userIds") Set<Long> userIds);
}