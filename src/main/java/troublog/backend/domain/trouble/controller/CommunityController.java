package troublog.backend.domain.trouble.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.request.CommentReqDto;
import troublog.backend.domain.trouble.dto.response.CommentDetailResDto;
import troublog.backend.domain.trouble.dto.response.CommentResDto;
import troublog.backend.domain.trouble.dto.response.CommunityListResDto;
import troublog.backend.domain.trouble.dto.response.PostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.LikePostResDto;
import troublog.backend.domain.trouble.dto.response.LikeResDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.service.facade.command.CommentCommandFacade;
import troublog.backend.domain.trouble.service.facade.command.LikeCommandFacade;
import troublog.backend.domain.trouble.service.facade.query.CommentQueryFacade;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.response.PageResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "커뮤니티", description = "커뮤니티 관련 API")
public class CommunityController {

	private final CommentCommandFacade commentCommandFacade;
	private final CommentQueryFacade commentQueryFacade;
	private final LikeCommandFacade likeCommandFacade;
	private final PostQueryFacade postQueryFacade;

	@GetMapping("/{postId}")
	@Operation(summary = "트러블슈팅 게시글 상세 조회 API", description =
		"게시글 ID로 공개된 트러블슈팅 게시글의 상세 정보를 조회합니다. 댓글은 별도 API로 조회해야 합니다." + "이 API 호출 시 최근 읽은 포스트의 기록으로 저장됩니다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostDetailsResDto.class)))
	public ResponseEntity<BaseResponse<PostDetailsResDto>> findCommunityPostDetailsOnly(
		@Authentication CustomAuthenticationToken token,
		@PathVariable Long postId
	) {
		PostDetailsResDto response = postQueryFacade.findCommunityPostDetailsById(token.getUserId(), postId);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/list")
	@Operation(summary = "트러블슈팅 게시글 목록 조회 API", description = "공개된 모든 트러블슈팅 게시글을 페이지네이션으로 조회합니다. 정렬 기준을 지정할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<CommunityListResDto>> getCommunityPosts(
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size,
		@Schema(
			description = "정렬 기준",
			allowableValues = {"likes", "latest"},
			defaultValue = "latest"
		)
		@RequestParam(defaultValue = "latest") String sortBy
	) {
		Pageable pageable = postQueryFacade.getPageableWithSorting(page, size, sortBy);
		Page<CommunityListResDto> response = postQueryFacade.getCommunityPosts(pageable);
		return ResponseUtils.page(response);
	}

	@GetMapping("/search")
	@Operation(summary = "트러블슈팅 게시글 검색", description = "키워드를 사용하여 공개된 트러블슈팅 게시글을 검색합니다. 제목, 내용, 태그 등에서 검색됩니다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<PostResDto>> searchPost(
		@RequestParam String keyword,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size
	) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<PostResDto> response = postQueryFacade.searchPostByKeyword(keyword, pageable);
		return ResponseUtils.page(response);
	}

	@PostMapping("/{postId}/comment")
	@Operation(summary = "댓글 생성 API", description = "해당하는 post의 댓글을 생성한다.")
	public ResponseEntity<BaseResponse<CommentResDto>> createComment(@PathVariable Long postId,
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody CommentReqDto commentReqDto) {
		CommentResDto response = commentCommandFacade.createComment(auth.getUserId(), postId, commentReqDto);
		return ResponseUtils.created(response);
	}

	@PutMapping("/{postId}/{commentId}")
	@Operation(summary = "댓글 수정 API", description = "해당하는 post의 댓글 내용을 수정한다.")
	public ResponseEntity<BaseResponse<CommentResDto>> updateComment(@PathVariable Long postId,
		@PathVariable Long commentId,
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody CommentReqDto commentReqDto) {
		CommentResDto response = commentCommandFacade.updateComment(auth.getUserId(), commentReqDto, commentId, postId);
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
	public ResponseEntity<PageResponse<CommentResDto>> getComments(
		@PathVariable long postId,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size

	) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<CommentResDto> comments = commentQueryFacade.getComments(postId, pageable);
		return ResponseUtils.page(comments);
	}

	@PostMapping("/{postId}/{commentId}")
	@Operation(summary = "대댓글 생성 API", description = "해당하는 post의 댓글의 대댓글을 생성한다.")
	public ResponseEntity<BaseResponse<CommentResDto>> createChildComment(@PathVariable Long commentId,
		@PathVariable Long postId,
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody CommentReqDto commentReqDto) {
		CommentResDto response = commentCommandFacade.createChildComment(auth.getUserId(), commentReqDto,
			commentId, postId);
		return ResponseUtils.created(response);
	}

	@GetMapping("/{postId}/{commentId}")
	@Operation(summary = "댓글 상세 조회 API", description = "해당하는 댓글과 댓글의 대댓글 전체를 최신순으로 조회한다.")
	public ResponseEntity<BaseResponse<CommentDetailResDto>> getDetailComment(
		@PathVariable Long commentId,
		@PathVariable Long postId
	) {
		CommentDetailResDto response = commentQueryFacade.getDetailComment(commentId, postId);
		return ResponseUtils.ok(response);
	}

	@PostMapping("/{postId}/like")
	@Operation(summary = "포스트 좋아요 API", description = "해당하는 포스트에 좋아요한다. 만약 좋아요가 눌러져 있을 시 자동으로 삭제된다. (like true 시 좋아요 눌러진 것/ false는 취소된 것")
	public ResponseEntity<BaseResponse<LikeResDto>> postLike(@PathVariable Long postId,
		@Authentication CustomAuthenticationToken auth) {
		LikeResDto response = likeCommandFacade.postLike(postId, auth.getUserId());
		return ResponseUtils.created(response);
	}

	@GetMapping("/likes")
	@Operation(summary = "좋아요한 포스트 조회 API", description = "최근에 좋아요한 순으로 포스트를 불러온다.")
	public ResponseEntity<PageResponse<LikePostResDto>> getUserLikedPosts(
		@Authentication CustomAuthenticationToken auth,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<LikePostResDto> likedPosts = likeCommandFacade.getLikedPostsByUser(auth.getUserId(), pageable);
		return ResponseUtils.page(likedPosts);
	}

	@GetMapping("/recent")
	@Operation(summary = "최근에 열람한 포스트 조회 API", description = "사용자가 최근에 열람한 순으로 포스트를 불러온다. (/community/{postId} 호출 기준)")
	public ResponseEntity<PageResponse<PostResDto>> getRecentlyViewedPosts(
		@Authentication CustomAuthenticationToken auth,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<PostResDto> posts = postQueryFacade.getRecentlyViewedPosts(auth.getUserId(), pageable);
		return ResponseUtils.page(posts);
	}

}
