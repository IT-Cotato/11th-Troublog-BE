package troublog.backend.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.dto.request.UserProfileUpdateReqDto;
import troublog.backend.domain.user.dto.response.UserFollowsResDto;
import troublog.backend.domain.user.dto.response.UserInfoResDto;
import troublog.backend.domain.user.dto.response.UserProfileResDto;
import troublog.backend.domain.user.service.UserFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "유저", description = "회원 정보 및 팔로잉/팔로워")
public class UserController {

	private final UserFacade userFacade;

	@PostMapping("/follow")
	@Operation(summary = "팔로우 걸기 API", description = "단방향 팔로우 걸기")
	public ResponseEntity<BaseResponse<Void>> follow(@RequestParam Long targetUserId,
		@Authentication CustomAuthenticationToken auth) {

		userFacade.followUser(targetUserId, auth.getUserId());

		return ResponseUtils.noContent();
	}

	@PostMapping("/unfollow")
	@Operation(summary = "언팔로우 API", description = "내가 건 팔로우 해제")
	public ResponseEntity<BaseResponse<Void>> unfollow(@RequestParam Long targetUserId,
		@Authentication CustomAuthenticationToken auth) {

		userFacade.unfollowUser(targetUserId, auth.getUserId());

		return ResponseUtils.noContent();
	}

	@GetMapping("/followers")
	@Operation(summary = "팔로워 목록 조회", description = "targetUser를 팔로우한 사용자들을 조회")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserFollowsResDto.class))))
	public ResponseEntity<BaseResponse<List<UserFollowsResDto>>> getFollowers(
		@Authentication CustomAuthenticationToken auth,
		@RequestParam Long targetUserId) {

		return ResponseUtils.ok(userFacade.getFollowers(auth.getUserId(), targetUserId));
	}

	@GetMapping("/followings")
	@Operation(summary = "팔로잉 목록 조회", description = "targetUser가 팔로우한 사용자들을 조회")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserFollowsResDto.class))))
	public ResponseEntity<BaseResponse<List<UserFollowsResDto>>> getFollowings(
		@Authentication CustomAuthenticationToken auth,
		@RequestParam Long targetUserId) {

		return ResponseUtils.ok(userFacade.getFollowings(auth.getUserId(), targetUserId));
	}

	@GetMapping("/{userId}")
	@Operation(summary = "사용자 정보 조회", description = "마이페이지 / 커뮤니티 사용자 정보 조회")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = UserInfoResDto.class)))
	public ResponseEntity<BaseResponse<UserInfoResDto>> getUserInfo(
		@PathVariable Long userId) {

		return ResponseUtils.ok(userFacade.getUserInfo(userId));
	}

	@GetMapping("")
	@Operation(summary = "내 프로필 조회", description = "마이페이지 내 프로필 조회")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = UserProfileResDto.class)))
	public ResponseEntity<BaseResponse<UserProfileResDto>> getMyProfile(
		@Authentication CustomAuthenticationToken auth) {

		return ResponseUtils.ok(userFacade.getMyProfile(auth.getUserId()));
	}

	@PatchMapping("")
	@Operation(summary = "내 프로필 수정", description = "마이페이지 내 프로필 수정")
	public ResponseEntity<BaseResponse<Void>> updateMyProfile(
		@Valid @RequestBody UserProfileUpdateReqDto userProfileUpdateReqDto,
		@Authentication CustomAuthenticationToken auth) {

		userFacade.updateMyProfile(auth.getUserId(), userProfileUpdateReqDto);

		return ResponseUtils.noContent();
	}

	@DeleteMapping("")
	@Operation(summary = "회원 탈퇴", description = "마이페이지 회원 탈퇴")
	public ResponseEntity<BaseResponse<Void>> deleteMyProfile(
		@Authentication CustomAuthenticationToken auth) {

		userFacade.deleteMyProfile(auth.getUserId());

		return ResponseUtils.noContent();
	}
}
