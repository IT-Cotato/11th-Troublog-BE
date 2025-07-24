package troublog.backend.domain.trouble.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.service.command.PostCommandService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/troubles")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "트러블슈팅", description = "트러블슈팅 문서 관련 엔드포인트")
public class PostController {

	private final PostCommandService postCommandService;
	private final PostQueryService postQueryService;

	@PostMapping
	@Operation(summary = "트러블슈팅 문서 생성 API", description = "트러블슈팅 문서를 새롭게 생성한다.")
	@ApiResponse(responseCode = "201", description = "",
		content = @Content(schema = @Schema(implementation = Long.class)))
	public ResponseEntity<BaseResponse<PostResDto>> createPost(
		@Authentication CustomAuthenticationToken token,
		@Valid @RequestBody PostCreateReqDto reqDto) {
		PostResDto response = postCommandService.createPost(reqDto, token.getUserId());
		return ResponseUtils.created(response);
	}

	@DeleteMapping("/{postId}")
	@Operation(summary = "트러블슈팅 문서 삭제 API", description = "트러블슈팅 문서를 삭제한다.")
	@ApiResponse(responseCode = "204", description = "No Content", content = @Content)
	public ResponseEntity<BaseResponse<Void>> deletePost(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId) {
		postCommandService.deletePost(postId);
		return ResponseUtils.noContent();
	}
}