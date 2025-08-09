package troublog.backend.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
@Tag(name = "OAuth", description = "OAuth 관련 API")
public class OAuth2Controller {

	@GetMapping("/kakao")
	public ResponseEntity<BaseResponse<Void>> kakaoLogin(@RequestParam("code") String accessCode,
		HttpServletResponse response) {
		return ResponseUtils.noContent();
	}
}
