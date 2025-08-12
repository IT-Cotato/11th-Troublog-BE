package troublog.backend.domain.user.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.user.converter.FollowConverter;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.dto.request.UserProfileUpdateReqDto;
import troublog.backend.domain.user.dto.response.PostCardUserInfoResDto;
import troublog.backend.domain.user.dto.response.UserFollowsResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.domain.user.dto.response.UserProfileResDto;
import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.command.FollowCommandService;
import troublog.backend.domain.user.service.command.UserCommandService;
import troublog.backend.domain.user.service.query.FollowQueryService;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.domain.user.validator.FollowValidator;
import troublog.backend.domain.user.validator.UserValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final FollowValidator followValidator;
	private final UserCommandService userCommandService;
	private final UserQueryService userQueryService;
	private final FollowCommandService followCommandService;
	private final FollowQueryService followQueryService;

	@Transactional
	public void followUser(Long followerId, Long followingId) {

		// 자기 자신은 팔로우 불가
		followValidator.validateNotSelfFollow(followerId, followingId);

		// 유저 존재 확인
		User follower = userQueryService.findUserByIdAndIsDeletedFalse(followerId);
		User following = userQueryService.findUserByIdAndIsDeletedFalse(followingId);

		// 이미 존재하는 팔로우 관계인지 확인
		followQueryService.existsByFollowerAndFollowing(follower, following);

		// 팔로우 관계 생성 (단방향)
		Follow follow = FollowConverter.toEntity(follower, following);

		followCommandService.save(follow);
	}

	@Transactional
	public void unfollowUser(Long followerId, Long followingId) {

		// 자기 자신은 언팔로우 불가
		followValidator.validateNotSelfFollow(followerId, followingId);

		// 유저 존재 확인
		User follower = userQueryService.findUserByIdAndIsDeletedFalse(followerId);
		User following = userQueryService.findUserByIdAndIsDeletedFalse(followingId);

		// 팔로우 관계인지 확인
		Follow follow = followQueryService.findByFollowerAndFollowing(follower, following);

		// 팔로우 관계 삭제
		followCommandService.delete(follow);
	}

	@Transactional(readOnly = true)
	public List<UserFollowsResDto> getFollowers(Long userId, Long targetUserId) {

		// 사용자 (본인) 조회
		User viewer = userQueryService.findUserByIdAndIsDeletedFalse(userId);

		// 커뮤니티 - 다른 사용자 조회 (본인 or 타인)
		User targetUser = userId.equals(targetUserId)
			? viewer
			: userQueryService.findUserByIdAndIsDeletedFalse(targetUserId);

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
	public List<UserFollowsResDto> getFollowings(Long userId, Long targetUserId) {

		// 사용자 (본인) 조회
		User viewer = userQueryService.findUserByIdAndIsDeletedFalse(userId);

		// 커뮤니티의 경우 - 다른 사용자 조회 (본인 or 타인)
		User targetUser = userId.equals(targetUserId)
			? viewer
			: userQueryService.findUserByIdAndIsDeletedFalse(targetUserId);

		// 다른 사용자의 팔로잉 리스트 조회
		List<User> followings = followQueryService.findFollowings(targetUser);

		// 사용자 (본인) 가 팔로우하고 있는 유저 목록 조회 → ID만 추출
		Set<Long> viewerFollowingIds = UserConverter.extractUserIds(
			followQueryService.findFollowings(viewer)
		);

		// DTO 변환
		return UserConverter.toUserFollowsDtoList(followings, viewerFollowingIds);
	}

	@Transactional(readOnly = true)
	public UserInfoResDto getUserInfo(Long userId) {

		// 사용자 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalse(userId);

		// 사용자의 팔로잉 목록 조회
		long followingNum = followQueryService.findFollowings(user).size();

		// 사용자의 팔로워 목록 조회
		long followerNum = followQueryService.findFollowers(user).size();

		// DTO 변환
		return UserConverter.toUserResDto(user, followerNum, followingNum);
	}

	@Transactional(readOnly = true)
	public Map<Long, PostCardUserInfoResDto> getUserInfoMap(Set<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyMap();
		}

		List<User> users = userQueryService.findAllByIds(userIds);

		return users.stream()
			.collect(Collectors.toMap(
				User::getId,
				UserConverter::toPostCardUserInfoResDto,
				(existing, replacement) -> existing));
	}

	@Transactional(readOnly = true)
	public UserProfileResDto getMyProfile(Long userId) {

		// 사용자 (본인) 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalse(userId);

		// DTO 변환
		return UserConverter.toUserProfileResDto(user);
	}

	@Transactional
	public void updateMyProfile(Long userId, UserProfileUpdateReqDto userProfileUpdateReqDto) {

		// 프로필 수정 요청 유효성 검사
		UserValidator.validateProfileUpdateRequest(userId, userProfileUpdateReqDto.userId());

		// 사용자 (본인) 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalse(userId);

		// 프로필 수정
		userCommandService.updateUserProfile(user, userProfileUpdateReqDto);
	}

	@Transactional
	public void deleteMyProfile(Long userId) {

		// 사용자 (본인) 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalse(userId);

		// 사용자 삭제
		userCommandService.deleteUser(user);
	}
}
