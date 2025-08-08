package troublog.backend.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import troublog.backend.domain.auth.dto.LoginResDto;
import troublog.backend.domain.auth.dto.LoginReqDto;
import troublog.backend.domain.auth.dto.RegisterDto;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.command.UserCommandService;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.constant.EnvType;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;
import troublog.backend.global.common.util.JwtProvider;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtProvider jwtProvider;

	public void setCookieRefreshToken(String refreshToken, HttpServletResponse response) {
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
