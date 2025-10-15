package troublog.backend.domain.terms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.terms.dto.request.TermsAgreementReqDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.facade.TermsFacade;
import troublog.backend.domain.terms.facade.TermsFacadeImpl;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsCommandController {

	private final TermsFacade termsFacade = new TermsFacadeImpl();

	@PostMapping("/agree")
	public ResponseEntity<BaseResponse<TermsAgreementResDto>> agreeToTerms(
		@RequestBody TermsAgreementReqDto request,
		@Authentication CustomAuthenticationToken token
	) {
		TermsAgreementResDto response = termsFacade.agreeToTerms(request, token.getUserId());
		return ResponseUtils.ok(response);
	}
}
