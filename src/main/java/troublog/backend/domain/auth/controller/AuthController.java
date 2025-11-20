package troublog.backend.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.auth.dto.LoginReqDto;
import troublog.backend.domain.auth.dto.LoginResDto;
import troublog.backend.domain.auth.dto.OAuth2RegisterReqDto;
import troublog.backend.domain.auth.dto.PasswordAuthCodeCheckReq;
import troublog.backend.domain.auth.dto.PasswordChangeReq;
import troublog.backend.domain.auth.dto.PasswordEmailCheckReq;
import troublog.backend.domain.auth.dto.PasswordEmailUUIDRes;
import troublog.backend.domain.auth.dto.RegisterReqDto;
import troublog.backend.domain.auth.dto.RegisterResDto;
import troublog.backend.domain.auth.service.AuthFacade;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "인증", description = "회원가입 및 로그인 API")
public class AuthController {

	private final AuthFacade authFacade;

	@PostMapping("/register")
	@Operation(summary = "회원가입 API", description = "깃헙 주소는 선택, 나머지는 필수입력")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = RegisterResDto.class)))
	public ResponseEntity<BaseResponse<RegisterResDto>> register(
		@Valid @RequestBody RegisterReqDto registerReqDto,
		HttpServletRequest request) {

		RegisterResDto registerResDto = authFacade.register(registerReqDto, request);

		return ResponseUtils.created(registerResDto);
	}

	@PostMapping("/email-check")
	@Operation(summary = "이메일 중복체크 API", description = "회원가입 첫번째 화면")
	public ResponseEntity<BaseResponse<Void>> checkDuplicateEmail(@RequestParam String email,
		HttpServletRequest request) {

		authFacade.checkDuplicateEmail(email, request);

		return ResponseUtils.noContent();
	}

	@Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = LoginResDto.class)))
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<LoginResDto>> login(
		@Valid @RequestBody LoginReqDto loginReqDto,
		HttpServletRequest request,
		HttpServletResponse response) {

		LoginResDto loginResDto = authFacade.login(loginReqDto, request, response);

		return ResponseUtils.ok(loginResDto);
	}

	@Operation(summary = "Access Token 재발급 API", description = "만료된 accessToken과 refreshToken을 통해 재발급"
		+ ", 개발단계에서 refreshToken은 임시로 header에 담아 전달")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = String.class)))
	@PostMapping("/refresh")
	public ResponseEntity<BaseResponse<String>> reissueAccessToken(HttpServletRequest request) {

		String accessToken = authFacade.reissueAccessToken(request);

		return ResponseUtils.ok(accessToken);
	}

	@Operation(summary = "로그아웃 API", description = "refreshToken을 전달해 로그아웃"
		+ ", 개발단계에서 refreshToken은 임시로 header에 담아 전달")
	@PostMapping("/logout")
	public ResponseEntity<BaseResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {

		authFacade.logout(request, response);

		return ResponseUtils.noContent();
	}

	@Operation(summary = "카카오 로그인 약관 동의 후 입력 란", description = "닉네임, 분야, 한 줄 소개, 깃허브 주소 입력")
	@PostMapping("/oauth-register")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = Long.class)))
	public ResponseEntity<BaseResponse<RegisterResDto>> oauthRegister(
		@Valid @RequestBody OAuth2RegisterReqDto oAuth2RegisterReqDto,
		HttpServletRequest request
	) {
		RegisterResDto response = authFacade.oAuthRegister(oAuth2RegisterReqDto, request);

		return ResponseUtils.created(response);
	}

	@PostMapping("/find-password")
	@Operation(summary = "비밀번호 찾기 이메일 인증", description = "이메일 입력시 해당 이메일로 인증코드 전송")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = PasswordEmailUUIDRes.class)))
	public ResponseEntity<BaseResponse<PasswordEmailUUIDRes>> findPassword(
		@Valid @RequestBody PasswordEmailCheckReq passwordEmailCheckReq, HttpServletRequest request) {

		PasswordEmailUUIDRes passwordEmailUUIDRes = authFacade.checkEmailForPassword(passwordEmailCheckReq, request);
		return ResponseUtils.ok(passwordEmailUUIDRes);
	}

	@PostMapping("/check-code")
	@Operation(summary = "비밀번호 찾기 인증번호 입력", description = "메일로 받은 6자리 인증번호와 서버로 "
		+ "부터 받은 가장 최신의 UUID를 입력")
	public ResponseEntity<BaseResponse<Void>> checkAuthCode(
		@Valid @RequestBody PasswordAuthCodeCheckReq passwordAuthCodeCheckReq, HttpServletRequest request) {

		authFacade.checkAuthCodePassword(passwordAuthCodeCheckReq, request);
		return ResponseUtils.noContent();
	}

	@PostMapping("/change-password")
	@Operation(summary = "비밀번호 재설정")
	public ResponseEntity<BaseResponse<Void>> changePassword(
		@Valid @RequestBody PasswordChangeReq passwordChangeReq, HttpServletRequest request) {

		authFacade.changePassword(passwordChangeReq, request);
		return ResponseUtils.noContent();
	}
}
