package troublog.backend.domain.user.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.user.converter.FollowConverter;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.dto.request.UserProfileUpdateReqDto;
import troublog.backend.domain.user.dto.response.PostCardUserInfoResDto;
import troublog.backend.domain.user.dto.response.UserFollowsResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.domain.user.dto.response.UserPostStatusResDto;
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
	private final PostQueryService postQueryService;

	@Transactional
	public void followUser(Long followerId, Long followingId) {

		// 자기 자신은 팔로우 불가
		followValidator.validateNotSelfFollow(followerId, followingId);

		// 유저 존재 확인
		User follower = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(followerId);
		User following = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(followingId);

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
		User follower = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(followerId);
		User following = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(followingId);

		// 팔로우 관계인지 확인
		Follow follow = followQueryService.findByFollowerAndFollowing(follower, following);

		// 팔로우 관계 삭제
		followCommandService.delete(follow);
	}

	@Transactional(readOnly = true)
	public List<UserFollowsResDto> getFollowers(Long userId, Long targetUserId) {

		// 사용자 (본인) 조회
		User viewer = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(userId);

		// 커뮤니티 - 다른 사용자 조회 (본인 or 타인)
		User targetUser = userId.equals(targetUserId)
			? viewer
			: userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(targetUserId);

		// 다른 사용자의 팔로워 리스트 조회
		List<User> followers = followQueryService.findFollowers(targetUser.getId());

		// 사용자 (본인) 가 팔로우하고 있는 유저 목록 조회 → ID만 추출
		Set<Long> viewerFollowingIds = UserConverter.extractUserIds(
			followQueryService.findFollowings(viewer.getId())
		);

		// DTO 변환
		return UserConverter.toUserFollowsDtoList(followers, viewerFollowingIds);
	}

	@Transactional(readOnly = true)
	public List<UserFollowsResDto> getFollowings(Long userId, Long targetUserId) {

		// 사용자 (본인) 조회
		User viewer = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(userId);

		// 커뮤니티의 경우 - 다른 사용자 조회 (본인 or 타인)
		User targetUser = userId.equals(targetUserId)
			? viewer
			: userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(targetUserId);

		// 다른 사용자의 팔로잉 리스트 조회
		List<User> followings = followQueryService.findFollowings(targetUser.getId());

		// 사용자 (본인) 가 팔로우하고 있는 유저 목록 조회 → ID만 추출
		Set<Long> viewerFollowingIds = UserConverter.extractUserIds(
			followQueryService.findFollowings(viewer.getId())
		);

		// DTO 변환
		return UserConverter.toUserFollowsDtoList(followings, viewerFollowingIds);
	}

	@Transactional(readOnly = true)
	public UserInfoResDto getUserInfo(Long userId, Long myId) {

		// 사용자 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(userId);

		// 사용자의 팔로잉 목록 조회
		List<User> followingUserList = followQueryService.findFollowings(user.getId());

		// 사용자의 팔로워 목록 조회
		long followerNum = followQueryService.findFollowers(user.getId()).size();

		// (본인)의 팔로잉 목록 조회
		List<User> myFollowingUserList = followQueryService.findFollowings(myId);

		// 사용자 (본인) 가 팔로우하고 있는지 여부 확인
		boolean isFollowed = myFollowingUserList.contains(user);

		// DTO 변환
		return UserConverter.toUserResDto(user, followerNum, followingUserList.size(), isFollowed);
	}

	@Transactional(readOnly = true)
	public UserPostStatusResDto getMyPostStatus(String postStatus, Long userId) {

		PostStatus status = null;
		if(StringUtils.hasText(postStatus)) {
			status = PostStatus.from(postStatus);
		}

		// 작성 상태에 따른 게시글 ID 리스트 조회
		List<Long> postIdList = postQueryService.findPostByStatusAndUserId(userId, status)
			.stream()
			.map(Post::getId)
			.toList();

		// DTO 변환
		return UserConverter.toUserPostStatusResDto(
			userId,
			postStatus,
			postIdList
		);
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
		User user = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(userId);

		// DTO 변환
		return UserConverter.toUserProfileResDto(user);
	}

	@Transactional
	public void updateMyProfile(Long userId, UserProfileUpdateReqDto userProfileUpdateReqDto) {

		// 프로필 수정 요청 유효성 검사
		UserValidator.validateProfileUpdateRequest(userId, userProfileUpdateReqDto.userId());

		// 사용자 (본인) 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(userId);

		// 프로필 수정
		userCommandService.updateUserProfile(user, userProfileUpdateReqDto);
	}

	@Transactional
	public void deleteMyProfile(Long userId) {

		// 사용자 (본인) 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(userId);

		// 사용자 삭제
		userCommandService.deleteUser(user);
	}
}
