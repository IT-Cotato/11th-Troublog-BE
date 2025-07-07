package troublog.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.ApiResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class TestController {

	/**
	 * Handles a GET request to the /test endpoint and returns a standardized success response.
	 *
	 * @return a ResponseEntity containing an ApiResponse with the message "성공"
	 */
	@GetMapping("")
	@Operation(summary = "테스트용 메서드")
	public ResponseEntity<ApiResponse<String>> apiTest(@Authentication CustomAuthenticationToken auth) {
		return ResponseUtils.ok("성공");
	}
}
