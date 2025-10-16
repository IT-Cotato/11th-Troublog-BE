package troublog.backend.domain.terms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class TermsQueryController {

	private final TermsQueryFacade facade;

	@GetMapping("/latest")
	public ResponseEntity<BaseResponse<LatestTermsResDto>> getLatestTerms() {
		LatestTermsResDto response = facade.getLatestTerms();
		return ResponseUtils.ok(response);
	}

	@GetMapping("/history")
	public ResponseEntity<BaseResponse<TermsAgreementResDto>> getUserTermsHistory(
		@Authentication CustomAuthenticationToken token
	) {
		TermsAgreementResDto response = facade.getUserTermsHistory(token.getUserId());
		return ResponseUtils.ok(response);
	}
}
