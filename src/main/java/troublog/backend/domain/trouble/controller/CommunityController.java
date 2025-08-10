package troublog.backend.domain.trouble.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.request.CommentReqDto;
import troublog.backend.domain.trouble.dto.request.CommentUpdateReqDto;
import troublog.backend.domain.trouble.dto.response.CommentDetailResDto;
import troublog.backend.domain.trouble.dto.response.CommentResDto;
import troublog.backend.domain.trouble.dto.response.LikePostResDto;
import troublog.backend.domain.trouble.dto.response.LikeResDto;
import troublog.backend.domain.trouble.service.facade.CommentCommandFacade;
import troublog.backend.domain.trouble.service.facade.CommentQueryFacade;
import troublog.backend.domain.trouble.service.facade.LikeCommandFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "커뮤니티", description = "커뮤니티 관련 API")
public class CommunityController {

	private final LikeCommandFacade likeService;
	private final CommentCommandFacade commentCommandFacade;
	private final CommentQueryFacade commentQueryFacade;
	private final LikeCommandFacade likeCommandService;

	@PostMapping("{postId}/comment")
	@Operation(summary = "댓글 생성 API", description = "해당하는 post의 댓글을 생성한다.")
	public ResponseEntity<BaseResponse<CommentResDto>> createComment(@PathVariable Long postId,
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody CommentReqDto commentReqDto) {
		CommentResDto response = commentCommandFacade.createComment(auth.getUserId(), commentReqDto);
		return ResponseUtils.created(response);
	}

	@PutMapping("{postId}/comment")
	@Operation(summary = "댓글 수정 API", description = "해당하는 post의 댓글 내용을 수정한다.")
	public ResponseEntity<BaseResponse<CommentResDto>> updateComment(@PathVariable Long postId,
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody CommentUpdateReqDto commentUpdateReqDto) {
		Long userId = auth.getUserId();
		CommentResDto response = commentCommandFacade.updateComment(userId, commentUpdateReqDto);
		return ResponseUtils.ok(response);
	}

	@DeleteMapping("/{commentId}/soft")
	@Operation(summary = "댓글 임시 삭제 API", description = "트러블슈팅의 댓글을 임시 삭제한다.")
	public ResponseEntity<BaseResponse<Void>> softDeleteComment(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long commentId
	) {
		commentCommandFacade.softDeleteComment(auth.getUserId(), commentId);
		return ResponseUtils.noContent();
	}

	@GetMapping("/{postId}/comments")
	@Operation(summary = "댓글 목록 조회 API", description = "해당하는 트러블슈팅의 댓글 전체를 최신순으로 조회한다.")
	public ResponseEntity<BaseResponse<List<CommentResDto>>> getComments(
		@PathVariable long postId
	) {
		List<CommentResDto> comments = commentQueryFacade.getComments(postId);
		return ResponseUtils.ok(comments);
	}

	@PostMapping("/{commentId}")
	@Operation(summary = "대댓글 생성 API", description = "해당하는 post의 댓글의 대댓글을 생성한다.")
	public ResponseEntity<BaseResponse<CommentResDto>> createChildComment(@PathVariable Long commentId,
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody CommentReqDto commentReqDto) {
		CommentResDto response = commentCommandFacade.createChildComment(auth.getUserId(), commentReqDto,
			commentId);
		return ResponseUtils.created(response);
	}

	@GetMapping("/{commentId}")
	@Operation(summary = "댓글 상세 조회 API", description = "해당하는 댓글과 댓글의 대댓글 전체를 최신순으로 조회한다.")
	public ResponseEntity<BaseResponse<CommentDetailResDto>> getDetailComment(
		@PathVariable long commentId
	) {
		CommentDetailResDto response = commentQueryFacade.getDetailComment(commentId);
		return ResponseUtils.ok(response);
	}

	@PostMapping("/{postId}")
	@Operation(summary = "포스트 좋아요 API", description = "해당하는 포스트에 좋아요한다.")
	public ResponseEntity<BaseResponse<LikeResDto>> postLike(@PathVariable Long postId,
		@Authentication CustomAuthenticationToken auth) {
		LikeResDto response = likeCommandService.postLike(postId, auth.getUserId());
		return ResponseUtils.created(response);
	}

	// 포스트 좋아요 취소

	// 좋아요한 포스트 조회
	@GetMapping("/likes")
	@Operation(summary = "좋아요한 포스트 조회 API", description = "최근에 좋아요한 순으로 포스트를 불러온다.")
	public ResponseEntity<BaseResponse<List<LikePostResDto>>> getUserLikedPosts(
		@Authentication CustomAuthenticationToken auth) {
		Long userId = auth.getUserId();
		List<LikePostResDto> likedPosts = likeService.getLikedPostsByUser(userId);
		return ResponseUtils.ok(likedPosts);
	}
}
