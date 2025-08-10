package troublog.backend.domain.auth.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.auth.dto.LoginReqDto;
import troublog.backend.domain.auth.dto.LoginResDto;
import troublog.backend.domain.auth.dto.OAuth2RegisterReqDto;
import troublog.backend.domain.auth.dto.RegisterReqDto;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.command.UserCommandService;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.constant.EnvType;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;
import troublog.backend.global.common.util.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthFacade {

	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;

	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Value("${spring.profiles.active}")
	private String profilesActive;

	@Transactional
	public Long register(RegisterReqDto registerReqDto, HttpServletRequest request) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 닉네임 중복 체크
		boolean isDuplicatedNickname = userQueryService.existsByNickname(registerReqDto.nickname());
		if (isDuplicatedNickname) {
			throw new UserException(ErrorCode.DUPLICATED_NICKNAME);
		}

		// 비밀번호 중복 체크
		checkDuplicateEmail(registerReqDto.email(), request);

		// 비밀번호 인코딩
		String encodedPassword = passwordEncoder.encode(registerReqDto.password());

		User user = UserConverter.toEntity(registerReqDto, encodedPassword);

		return userCommandService.save(user).getId();
	}

	@Transactional
	public LoginResDto login(LoginReqDto loginReqDto, HttpServletRequest request, HttpServletResponse response) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 유저 확인
		User user = userQueryService.findUserByEmailAndIsDeletedFalse(loginReqDto.email());

		// 비밀번호 검증
		if (!passwordEncoder.matches(loginReqDto.password(), user.getPassword())) {
			throw new UserException(ErrorCode.INVALID_USER);
		}

		// Authentication 객체 생성
		// 유저 이메일(아이디), 비밀번호 외에 유저 아이디, 닉네임, 프론트 쪽 환경변수도 claim 으로 넣어주는 CustomAuthenticationToken
		CustomAuthenticationToken authenticationToken =
			CustomAuthenticationToken.unauthenticated(loginReqDto.email(), loginReqDto.password(), user.getId(),
				clientEnvType,
				user.getNickname());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		// 액세스토큰, 리프레시토큰 생성
		String accessToken = jwtProvider.createAuthToken(authentication);
		String localToken = jwtProvider.createAuthToken(authentication);
		String refreshToken = jwtProvider.createRefreshToken(authenticationToken);

		// 리프레시 토큰 Set-Cookie로 내려줌
		jwtProvider.setCookieRefreshToken(refreshToken, response);

		return (profilesActive.equals(EnvType.LOCAL.getEnvType()) || clientEnvType.equals(EnvType.LOCAL.getEnvType()))
			? LoginResDto.localReturn(user.getId(), accessToken, refreshToken, localToken)
			: LoginResDto.nonLocalReturn(user.getId(), accessToken, refreshToken);
	}

	@Transactional
	public String reissueAccessToken(HttpServletRequest request) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// jwtProvider 에서 액세스토큰의 만료시간, 리프레시토큰의 유효성 검증
		Long userId = jwtProvider.reissueAccessToken(request);

		// 새로운 액세스토큰에 넣어 줄 유저 정보 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalse(userId);

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
		jwtProvider.deleteCookieRefreshToken(response);
	}

	@Transactional(readOnly = true)
	public void checkDuplicateEmail(String email, HttpServletRequest request) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		boolean isDuplicated = userQueryService.existsByEmail(email);
		if (isDuplicated) {
			throw new UserException(ErrorCode.DUPLICATED_EMAIL);
		}
	}

	@Transactional
	public Long oAuthRegister(OAuth2RegisterReqDto oAuth2RegisterReqDto, HttpServletRequest request) {

		String clientEnvType = request.getHeader("EnvType");

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 닉네임 중복 체크
		boolean isDuplicatedNickname = userQueryService.existsByNickname(oAuth2RegisterReqDto.nickname());
		if (isDuplicatedNickname) {
			throw new UserException(ErrorCode.DUPLICATED_NICKNAME);
		}

		User user = userQueryService.findUserById(oAuth2RegisterReqDto.userId());
		user.updateOAuth2Info(oAuth2RegisterReqDto.nickname(), oAuth2RegisterReqDto.field(), oAuth2RegisterReqDto.bio(),
			oAuth2RegisterReqDto.githubUrl());

		return user.getId();
	}
}
