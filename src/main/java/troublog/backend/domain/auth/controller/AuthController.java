package troublog.backend.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.auth.dto.LoginResDto;
import troublog.backend.domain.auth.dto.LoginReqDto;
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.domain.auth.service.AuthService;
import troublog.backend.global.common.response.ApiResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	@Operation(summary = "회원가입 API", description = "깃헙 주소는 선택, 나머지는 필수입력")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = Long.class)))
	public ResponseEntity<ApiResponse<Long>> register(
		@RequestBody RegisterDto registerDto,
		HttpServletRequest request) {

		Long userId = authService.register(registerDto, request);

		return ResponseUtils.created(userId);
	}

	@Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = LoginResDto.class)))
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResDto>> login(
		@Valid @RequestBody LoginReqDto loginReqDto,
		HttpServletRequest request,
		HttpServletResponse response) {

		LoginResDto loginResDto = authService.login(loginReqDto, request, response);

		return ResponseUtils.ok(loginResDto);
	}

	@Operation(summary = "Access Token 재발급 API", description = "만료된 accessToken과 refreshToken을 통해 재발급"
		+ ", 개발단계에서 refreshToken은 임시로 header에 담아 전달")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = String.class)))
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<String>> reissueAccessToken(HttpServletRequest request) {

		String accessToken = authService.reissueAccessToken(request);

		return ResponseUtils.ok(accessToken);
	}

	@Operation(summary = "로그아웃 API", description = "refreshToken을 전달해 로그아웃"
		+ ", 개발단계에서 refreshToken은 임시로 header에 담아 전달")
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {

		authService.logout(request, response);

		return ResponseUtils.noContent();
	}
}
