package troublog.backend.domain.user.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.converter.FollowConverter;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.dto.FollowDto;
import troublog.backend.domain.user.dto.UserDto;
import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserQueryService userQueryService;
	private final FollowCommandService followCommandService;
	private final FollowQueryService followQueryService;

	@Transactional
	public void followUser(Long followerId, Long followingId) {

		// 유저 조회
		FollowDto followDto = userQueryService.getFollows(followerId, followingId);

		// 이미 존재하는 팔로우 관계인지 확인
		followQueryService.existsByFollowerAndFollowing(followDto.follower(), followDto.following());

		// 팔로우 관계 생성 (단방향)
		Follow follow = FollowConverter.toEntity(followDto.follower(), followDto.following());

		followCommandService.save(follow);
	}

	@Transactional
	public void unfollowUser(Long followerId, Long followingId) {

		// 유저 조회
		FollowDto followDto = userQueryService.getFollows(followerId, followingId);

		// 팔로우 관계인지 확인
		Follow follow = followQueryService.findByFollowerAndFollowing(followDto.follower(), followDto.following());

		// TODO : 이건 hard delete가 맞겠져?
		followCommandService.delete(follow);
	}

	@Transactional(readOnly = true)
	public List<UserDto.UserFollowsDto> getFollowers(Long userId, Long targetUserId) {

		// 사용자 (본인) 조회
		User viewer = userQueryService.findUserById(userId);

		// 커뮤니티 - 다른 사용자 조회 (본인 or 타인)
		User targetUser = userId.equals(targetUserId)
			? viewer
			: userQueryService.findUserById(targetUserId);

		// 다른 사용자의 팔로워 리스트 조회
		List<User> followers = followQueryService.findFollowers(targetUser);

		// 사용자 (본인) 가 팔로우하고 있는 유저 목록 조회 → ID만 추출
		Set<Long> viewerFollowingIds = UserConverter.extractUserIds(
			followQueryService.findFollowings(viewer)
		);

		// DTO 변환
		return UserConverter.toUserFollowsDtoList(followers, viewerFollowingIds);
	}

	@Transactional(readOnly = true)
	public List<UserDto.UserFollowsDto> getFollowings(Long userId, Long targetUserId) {

		// 사용자 (본인) 조회
		User viewer = userQueryService.findUserById(userId);

		// 커뮤니티의 경우 - 다른 사용자 조회 (본인 or 타인)
		User targetUser = userId.equals(targetUserId)
			? viewer
			: userQueryService.findUserById(targetUserId);

		// 다른 사용자의 팔로잉 리스트 조회
		List<User> followings = followQueryService.findFollowings(targetUser);

		// 사용자 (본인) 가 팔로우하고 있는 유저 목록 조회 → ID만 추출
		Set<Long> viewerFollowingIds = UserConverter.extractUserIds(
			followQueryService.findFollowings(viewer)
		);

		// DTO 변환
		return UserConverter.toUserFollowsDtoList(followings, viewerFollowingIds);
	}
}
