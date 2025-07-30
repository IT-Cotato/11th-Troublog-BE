package troublog.backend.domain.user.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.domain.user.dto.response.UserFollowsResDto;
import troublog.backend.domain.user.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UserConverter {

	public User toEntity(RegisterDto registerDto, String encodedPassword) {
		return User.builder()
			.email(registerDto.email())
			.password(encodedPassword)
			.nickname(registerDto.nickname())
			.field(registerDto.field())
			.bio(registerDto.bio())
			.githubUrl(registerDto.githubUrl())
			.build();
	}

	public Set<Long> extractUserIds(List<User> users) {
		return users.stream()
			.map(User::getId)
			.collect(Collectors.toSet());
	}

	public List<UserFollowsResDto> toUserFollowsDtoList(List<User> users, Set<Long> viewerFollowingIds) {
		return users.stream()
			.map(user -> toUserFollowsDto(user, viewerFollowingIds))
			.toList();
	}

	public UserFollowsResDto toUserFollowsDto(User user, Set<Long> viewerFollowingIds) {
		return new UserFollowsResDto(
			user.getId(),
			user.getNickname(),
			user.getEmail(),
			user.getProfileUrl(),
			viewerFollowingIds.contains(user.getId())
		);
	}
}
