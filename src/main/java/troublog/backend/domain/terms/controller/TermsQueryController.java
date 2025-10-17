package troublog.backend.domain.terms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.facade.query.TermsQueryFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
@Tag(name = "이용약관", description = "이용약관 조회 API")
public class TermsQueryController {

	private final TermsQueryFacade facade;

	@GetMapping("/latest")
	@Operation(
		summary = "최신 약관 조회",
		description = "현재 시점의 최신 약관 목록을 조회합니다. 회원가입 시 사용자에게 표시할 약관 정보를 가져옵니다."
	)
	@ApiResponse(
		responseCode = "200",
		description = "조회 성공",
		content = @Content(schema = @Schema(implementation = LatestTermsResDto.class))
	)
	public ResponseEntity<BaseResponse<LatestTermsResDto>> getLatestTerms() {
		LatestTermsResDto response = facade.getLatestTerms();
		return ResponseUtils.ok(response);
	}

	@GetMapping("/history")
	@Operation(
		summary = "내 약관 동의 내역 조회",
		description = "로그인한 사용자의 약관 동의 이력을 조회합니다. 사용자가 동의한 모든 약관 내역을 시간순으로 확인할 수 있습니다."
	)
	@ApiResponse(
		responseCode = "200",
		description = "조회 성공",
		content = @Content(schema = @Schema(implementation = TermsAgreementResDto.class))
	)
	@ApiResponse(
		responseCode = "401",
		description = "인증 실패"
	)
	public ResponseEntity<BaseResponse<TermsAgreementResDto>> getUserTermsHistory(
		@Authentication CustomAuthenticationToken token
	) {
		TermsAgreementResDto response = facade.getUserTermsHistory(token.getUserId());
		return ResponseUtils.ok(response);
	}
}
