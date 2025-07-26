package troublog.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.user.dto.UserDto;
import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;

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

	@Query("""
    SELECT f.follower FROM Follow f
    WHERE f.following.id = :viewerUserId
    ORDER BY f.follower.id DESC
""")
	List<User> findFollowings(@Param("viewerUserId") Long viewerUserId);
}
