package troublog.backend.domain.trouble.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.service.facade.PostQueryFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.response.PageResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/troubles")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "트러블슈팅", description = "트러블슈팅 문서 관련 엔드포인트")
public class PostQueryController {

	private final PostQueryFacade postQueryFacade;

	@GetMapping("/{postId}/combine")
	@Operation(summary = "트러블슈팅 문서 + AI 요약본 상세 조회 API", description = "ID 값 기반 트러블슈팅 문서 + AI 요약본 상세 조회")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<PostResDto>> findPostDetailsWithSummary(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId
	) {
		PostResDto response = postQueryFacade.findPostDetailsWithSummaryById(token.getUserId(), postId);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/{postId}/summary")
	@Operation(summary = "AI 요약본 상세 조회 API", description = "ID 값, SummaryType 기반 AI 요약본 상세 조회")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<PostResDto>> findPostSummaryOnly(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId,
		@Schema(
			allowableValues = {
				"RESUME",
				"BLOG",
				"INTERVIEW",
				"ISSUE_MANAGEMENT",
			}
		)
		@RequestParam String type
	) {
		PostResDto response = postQueryFacade.findPostSummaryById(token.getUserId(), postId, type);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/{postId}")
	@Operation(summary = "트러블슈팅 문서 상세 조회 API", description = "ID 값 기반 트러블슈팅 문서 상세 조회")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostResDto.class)))
	public ResponseEntity<BaseResponse<PostResDto>> findPostDetailsOnly(
		@Authentication CustomAuthenticationToken token,
		@PathVariable long postId
	) {
		PostResDto response = postQueryFacade.findPostDetailsById(token.getUserId(), postId);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/tags/category")
	@Operation(summary = "트러블슈팅 기술 태그 조회 API (카테고리)", description = "태그 카테고리 기반 기술태그를 조회한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = String.class)))
	public ResponseEntity<BaseResponse<List<String>>> findPostTags(
		@Schema(
			defaultValue = "FRONTEND",
			allowableValues = {
				"FRONTEND",
				"BACKEND",
				"DATABASE",
				"DEVOPS",
				"INFRA",
				"TOOL"
			}
		)
		@RequestParam(defaultValue = "FRONTEND") String tagCategory
	) {
		List<String> response = postQueryFacade.findPostTagsByCategory(tagCategory);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/tags")
	@Operation(summary = "트러블슈팅 기술 태그 조회 API (키워드)", description = "키워드 기반 기술태그를 조회한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = String.class)))
	public ResponseEntity<BaseResponse<List<String>>> findPostTagsByName(
		@RequestParam String tagName
	) {
		List<String> response = postQueryFacade.findPostTagsByName(tagName);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/my")
	@Operation(summary = "사용자의 트러블슈팅 문서 기반 검색 API", description = "로그인한 사용자의 모든 트러블 슈팅 문서를 키워드를 기반으로 검색한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<PostResDto>> searchUserPost(
		@Authentication CustomAuthenticationToken token,
		@RequestParam String keyword,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<PostResDto> response = postQueryFacade.searchUserPostByKeyword(token.getUserId(), keyword, pageable);
		return ResponseUtils.page(response);
	}

	@GetMapping("/search")
	@Operation(summary = "트러블슈팅 문서 검색 API", description = "키워드를 기반으로  트러블 슈팅 문서를 검색한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<PostResDto>> searchPost(
		@Authentication CustomAuthenticationToken token,
		@RequestParam String keyword,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<PostResDto> response = postQueryFacade.searchPostByKeyword(keyword, pageable);
		return ResponseUtils.page(response);
	}
}
