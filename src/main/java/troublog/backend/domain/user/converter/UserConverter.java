package troublog.backend.domain.user.converter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.auth.dto.RegisterReqDto;
import troublog.backend.domain.user.dto.response.UserFollowsResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.domain.user.dto.response.UserProfileResDto;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;

@UtilityClass
public class UserConverter {

	public User toEntity(RegisterReqDto registerReqDto, String encodedPassword) {
		return User.builder()
			.email(registerReqDto.email())
			.password(encodedPassword)
			.nickname(registerReqDto.nickname())
			.field(registerReqDto.field())
			.bio(registerReqDto.bio())
			.githubUrl(registerReqDto.githubUrl())
			.isDeleted(false)
			.status(UserStatus.ACTIVE)
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
