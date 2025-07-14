package troublog.backend.domain.auth.service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.auth.dto.LoginResDto;
import troublog.backend.domain.auth.dto.LoginReqDto;
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.UserRepository;
import troublog.backend.domain.user.service.UserCommandService;
import troublog.backend.domain.user.service.UserQueryService;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;
import troublog.backend.global.common.util.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Transactional
	public Long register(RegisterDto registerDto, HttpServletRequest request) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 비밀번호 인코딩
		String encodedPassword = passwordEncoder.encode(registerDto.password());

		User user = User.registerUser(registerDto, encodedPassword);

		return userCommandService.save(user);
	}

	@Transactional
	public LoginResDto login(LoginReqDto loginReqDto, HttpServletRequest request, HttpServletResponse response) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 유저 확인
		User user = userQueryService.findUserByEmail(loginReqDto.email());

		// 비밀번호 검증
		if(!passwordEncoder.matches(loginReqDto.password(), user.getPassword())) {
			throw new UserException(ErrorCode.INVALID_USER);
		}

		// Authentication 객체 생성
		// 유저 이메일(아이디), 비밀번호 외에 유저 아이디, 닉네임, 프론트 쪽 환경변수도 claim 으로 넣어주는 CustomAuthenticationToken
		CustomAuthenticationToken authenticationToken =
			CustomAuthenticationToken.unauthenticated(loginReqDto.email(), loginReqDto.password(), user.getId(), clientEnvType,
				user.getNickname());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		// 액세스토큰, 리프레시토큰 생성
		String accessToken = jwtProvider.createAuthToken(authentication);
		String localToken = jwtProvider.createAuthToken(authentication);
		String refreshToken = jwtProvider.createRefreshToken(authenticationToken);

		// 리프레시 토큰 Set-Cookie로 내려줌
		setCookieRefreshToken(refreshToken, response);

		return LoginResDto.of(user.getId(), accessToken, refreshToken, localToken);
	}

	private void setCookieRefreshToken(String refreshToken, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(false)
			.path("/")
			.maxAge(Duration.ofSeconds(86400))
			.sameSite("Strict")
			.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	@Transactional
	public String reissueAccessToken(HttpServletRequest request) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// jwtProvider 에서 액세스토큰의 만료시간, 리프레시토큰의 유효성 검증
		Long userId = jwtProvider.reissueAccessToken(request);

		// 새로운 액세스토큰에 넣어 줄 유저 정보 조회
		User user = userQueryService.findUserById(userId);

		//새로운 CustomAuthenticationToken 객체 생성
		CustomAuthenticationToken authenticationToken =
			CustomAuthenticationToken.unauthenticated(
				user.getEmail(),
				user.getPassword(),
				user.getId(),
				clientEnvType,
				user.getNickname());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		// 새로운 액세스토큰 생성
		return jwtProvider.createAuthToken(authentication);
	}

	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 로그아웃
		jwtProvider.logout(request);

		// set-cookie 에서 삭제
		deleteCookieRefreshToken(response);
	}

	private void deleteCookieRefreshToken(HttpServletResponse response) {
		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
			.path("/")
			.maxAge(0)  // 즉시 만료
			.httpOnly(true)
			.secure(false)
			.sameSite("Strict")
			.build();

		response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
	}
}
