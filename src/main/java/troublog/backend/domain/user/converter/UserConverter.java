package troublog.backend.domain.user.converter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.domain.user.dto.UserDto;
import troublog.backend.domain.user.entity.User;

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

	public List<UserDto.UserFollowsDto> toUserFollowsDtoList(List<User> users, Set<Long> viewerFollowingIds) {
		return users.stream()
			.map(user -> toUserFollowsDto(user, viewerFollowingIds))
			.toList();
	}

	public UserDto.UserFollowsDto toUserFollowsDto(User user, Set<Long> viewerFollowingIds) {
		return new UserDto.UserFollowsDto(
			user.getId(),
			user.getNickname(),
			user.getEmail(),
			user.getProfileUrl(),
			viewerFollowingIds.contains(user.getId())
		);
	}
}
