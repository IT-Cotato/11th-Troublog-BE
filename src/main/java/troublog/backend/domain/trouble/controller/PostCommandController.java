package troublog.backend.domain.trouble.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.service.facade.PostCommandFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/troubles")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "트러블슈팅", description = "트러블슈팅 문서 관련 엔드포인트")
public class PostCommandController {

	private final PostCommandFacade postCommandFacade;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "트러블슈팅 문서 생성 API", description = "트러블슈팅 문서를 새롭게 생성한다.")
	@ApiResponse(responseCode = "201", description = "CREATED",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<PostResDto>> createPost(
		@Authentication CustomAuthenticationToken token,
		@Valid @RequestBody PostReqDto postReqDto // 단순하게 JSON만
	) {
		PostResDto response = postCommandFacade.createPost(token.getUserId(), postReqDto);
		return ResponseUtils.created(response);
	}

	@PutMapping(path = "/{postId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "트러블슈팅 문서 수정 API", description = "트러블슈팅 문서를 수정한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<PostResDto>> updatePost(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId,
		@Valid @RequestBody PostReqDto postReqDto // 단순하게 JSON만
	) {
		PostResDto response = postCommandFacade.updatePost(token.getUserId(), postId, postReqDto);
		return ResponseUtils.ok(response);
	}

	@DeleteMapping("/{postId}")
	@Operation(summary = "트러블슈팅 문서 임시 삭제 API", description = "트러블슈팅 문서를 임시 삭제한다.")
	@ApiResponse(responseCode = "204", description = "No Content", content = @Content)
	public ResponseEntity<BaseResponse<Void>> deletePost(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId
	) {
		postCommandFacade.softDeletePost(token.getUserId(), postId);
		return ResponseUtils.noContent();
	}

	@PostMapping("/{postId}/restore")
	@Operation(summary = "트러블슈팅 문서 복구 API", description = "임시 삭제된 트러블슈팅 문서를 복구한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<PostResDto>> restorePost(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId
	) {
		PostResDto response = postCommandFacade.restorePost(token.getUserId(), postId);
		return ResponseUtils.ok(response);
	}
}