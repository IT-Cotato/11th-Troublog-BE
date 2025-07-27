package troublog.backend.domain.user.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;

@UtilityClass
public class FollowConverter {

	public Follow toEntity(User follower, User following) {
		return Follow.builder()
			.follower(follower)
			.following(following)
			.build();
	}
}
