package troublog.backend.domain.terms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.UserTermsHistoryResDto;
import troublog.backend.domain.terms.facade.TermsFacade;
import troublog.backend.domain.terms.facade.TermsFacadeImpl;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsQueryController {

	private final TermsFacade termsFacade = new TermsFacadeImpl();

	@GetMapping("/latest")
	public ResponseEntity<BaseResponse<LatestTermsResDto>> getLatestTerms() {
		LatestTermsResDto response = termsFacade.getLatestTerms();
		return ResponseUtils.ok(response);
	}

	@GetMapping("/history")
	public ResponseEntity<BaseResponse<UserTermsHistoryResDto>> getUserTermsHistory(
		@Authentication CustomAuthenticationToken token
	) {
		UserTermsHistoryResDto response = termsFacade.getUserTermsHistory(token.getUserId());
		return ResponseUtils.ok(response);
	}
}
