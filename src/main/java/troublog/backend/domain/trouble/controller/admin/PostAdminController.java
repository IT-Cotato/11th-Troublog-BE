package troublog.backend.domain.trouble.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.service.facade.command.CommentCommandFacade;
import troublog.backend.domain.trouble.service.facade.command.PostCommandFacade;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/troubles")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "트러블슈팅 (관리자)", description = "관리자용 트러블슈팅 문서 관련 엔드포인트")
public class PostAdminController {

	private final PostCommandFacade postCommandFacade;
	private final PostQueryFacade postQueryFacade;
	private final CommentCommandFacade commentCommandFacade;

	@DeleteMapping("/{postId}/hard")
	@Operation(summary = "트러블슈팅 문서 영구 삭제 API", description = "트러블슈팅 문서를 영구적으로 삭제한다. (관리자용)")
	@ApiResponse(responseCode = "204", description = "No Content", content = @Content)
	public ResponseEntity<BaseResponse<Void>> hardDeletePost(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId) {
		postCommandFacade.hardDeletePost(token.getUserId(), postId);
		return ResponseUtils.noContent();
	}

	@GetMapping("/deleted")
	@Operation(summary = "삭제된 트러블슈팅 문서 조회 API", description = "임시 삭제된 트러블슈팅 문서들을 조회한다. (관리자용)")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<List<PostResDto>>> findDeletedPosts() {
		List<PostResDto> response = postQueryFacade.findDeletedPosts();
		return ResponseUtils.ok(response);
	}

	@GetMapping("/active")
	@Operation(summary = "삭제되지 않은 트러블슈팅 문서 조회 API", description = "삭제되지 않은 모든 트러블슈팅 문서를 조회한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<List<PostResDto>>> findActivePosts() {
		List<PostResDto> response = postQueryFacade.findAllNotDeletedPosts();
		return ResponseUtils.ok(response);
	}

	@DeleteMapping("/comment/{commentId}/hard")
	@Operation(summary = "댓글 영구 삭제 API", description = "트러블슈팅의 댓글을 영구 삭제한다. (관리자용)")
	@ApiResponse(responseCode = "204", description = "No Content", content = @Content)
	public ResponseEntity<BaseResponse<Void>> hardDeleteComment(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long commentId
	) {
		commentCommandFacade.hardDeleteComment(auth.getUserId(), commentId);
		return ResponseUtils.noContent();
	}

}
