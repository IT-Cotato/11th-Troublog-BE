package troublog.backend.domain.user.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.domain.user.dto.response.UserFollowsResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.domain.user.dto.response.UserProfileResDto;
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
			.isDeleted(true)
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
		return UserFollowsResDto.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.email(user.getEmail())
			.profileUrl(user.getProfileUrl())
			.isFollowed(viewerFollowingIds.contains(user.getId()))
			.build();
	}

	public static UserInfoResDto toUserResDto(User user, long followerNum, long followingNum) {
		return UserInfoResDto.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.profileUrl(user.getProfileUrl())
			.bio(user.getBio())
			.followerNum(followerNum)
			.followingNum(followingNum)
			.build();
	}

	public static UserProfileResDto toUserProfileResDto(User user) {
		return UserProfileResDto.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.githubUrl(user.getGithubUrl())
			.bio(user.getBio())
			.build();
	}
}
