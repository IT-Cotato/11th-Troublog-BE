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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.response.CombineResDto;
import troublog.backend.domain.trouble.dto.response.PostCardResDto;
import troublog.backend.domain.trouble.dto.response.PostDetailsResDto;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;
import troublog.backend.domain.trouble.service.facade.query.PostSummaryQueryFacade;
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
	private final PostSummaryQueryFacade postSummaryQueryFacade;

	@GetMapping("/summary/{summaryId}")
	@Operation(summary = "AI 요약본 상세 조회 API", description = "ID 값 기반 트러블 슈팅 AI 요약본 상세 조회")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostSummaryResDto.class)))
	public ResponseEntity<BaseResponse<PostSummaryResDto>> findPostSummaryOnly(
		@Authentication CustomAuthenticationToken token,
		@PathVariable Long summaryId
	) {
		PostSummaryResDto response = postSummaryQueryFacade.findPostSummaryById(token.getUserId(), summaryId);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/{postId}")
	@Operation(summary = "트러블슈팅 문서 상세 조회 API", description = "ID 값 기반 트러블슈팅 문서 상세 조회")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PostDetailsResDto.class)))
	public ResponseEntity<BaseResponse<PostDetailsResDto>> findPostDetailsOnly(
		@Authentication CustomAuthenticationToken token,
		@PathVariable Long postId
	) {
		PostDetailsResDto response = postQueryFacade.findPostById(postId, token.getUserId());
		return ResponseUtils.ok(response);
	}

	@GetMapping("/{postId}/combine/{summaryId}")
	@Operation(summary = "트러블슈팅 문서 + AI 요약본 상세 조회 API", description = "ID 값 기반 트러블슈팅 문서 + AI 요약본 상세 조회")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = CombineResDto.class)))
	public ResponseEntity<BaseResponse<CombineResDto>> findPostDetailsWithSummary(
		@Authentication CustomAuthenticationToken token,
		@PathVariable Long postId,
		@PathVariable Long summaryId
	) {
		CombineResDto response = postQueryFacade.findPostDetailsWithSummaryById(token.getUserId(), postId, summaryId);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/tags")
	@Operation(summary = "트러블슈팅 기술 태그 조회 API (키워드)", description = "키워드 기반 기술태그를 조회한다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class))))
	public ResponseEntity<BaseResponse<List<String>>> findPostTagsByName(
		@RequestParam String tagName
	) {
		List<String> response = postQueryFacade.findPostTagsByName(tagName);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/my/search")
	@Operation(summary = "내 트러블슈팅 문서 검색 API",
		description = "로그인한 사용자의 트러블슈팅 문서를 키워드 기반으로 검색합니다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<PostCardResDto>> searchMyPosts(
		@Authentication CustomAuthenticationToken token,
		@RequestParam String keyword,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size
	) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<PostCardResDto> response = postQueryFacade.searchUserPostByKeyword(token.getUserId(), keyword, pageable);
		return ResponseUtils.page(response);
	}

	@GetMapping("/my/list")
	@Operation(summary = "내 트러블슈팅 문서 목록 조회 API",
		description = "로그인한 사용자의 트러블슈팅 문서 목록을 페이지네이션 및 정렬 기준으로 조회합니다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<TroubleListResDto>> getMyTroubles(
		@Authentication CustomAuthenticationToken auth,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size,
		@Schema(
			description = "정렬 기준",
			allowableValues = {"important", "latest"},
			defaultValue = "latest"
		)
		@RequestParam(defaultValue = "latest") String sortBy
	) {
		Pageable pageable = postQueryFacade.getPageableWithSorting(page, size, sortBy);
		Page<TroubleListResDto> response = postQueryFacade.getAllTroubles(auth.getUserId(), pageable);
		return ResponseUtils.page(response);
	}

	@GetMapping("/users/{userId}/search")
	@Operation(summary = "특정 사용자의 트러블슈팅 문서 검색 API",
		description = "지정된 사용자의 트러블슈팅 문서를 키워드 기반으로 검색합니다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<PostCardResDto>> searchUserPosts(
		@PathVariable Long userId,
		@RequestParam String keyword,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size
	) {
		Pageable pageable = postQueryFacade.getPageable(page, size);
		Page<PostCardResDto> response = postQueryFacade.searchUserPostByKeyword(userId, keyword, pageable);
		return ResponseUtils.page(response);
	}

	@GetMapping("/users/{userId}/list")
	@Operation(summary = "특정 사용자의 트러블슈팅 문서 목록 조회 API",
		description = "지정된 사용자의 트러블슈팅 문서 목록을 페이지네이션 및 정렬 기준으로 조회합니다.")
	@ApiResponse(responseCode = "200", description = "OK",
		content = @Content(schema = @Schema(implementation = PageResponse.class)))
	public ResponseEntity<PageResponse<TroubleListResDto>> getUserTroubles(
		@PathVariable Long userId,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size,
		@Schema(
			description = "정렬 기준",
			allowableValues = {"important", "latest"},
			defaultValue = "latest"
		)
		@RequestParam(defaultValue = "latest") String sortBy
	) {
		Pageable pageable = postQueryFacade.getPageableWithSorting(page, size, sortBy);
		Page<TroubleListResDto> response = postQueryFacade.getAllTroubles(userId, pageable);
		return ResponseUtils.page(response);
	}
}
