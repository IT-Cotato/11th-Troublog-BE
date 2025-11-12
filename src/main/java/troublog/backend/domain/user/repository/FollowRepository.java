package troublog.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;

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

	@Query("""
	SELECT f
	FROM Follow f
	WHERE f.follower = :user OR f.following = :user
""")
	List<Follow> findAllByFollowerOrFollowing(@Param("user") User user);
}