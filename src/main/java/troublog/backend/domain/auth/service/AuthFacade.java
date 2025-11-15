package troublog.backend.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.converter.AlertConverter;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.service.AlertCommandService;
import troublog.backend.domain.auth.dto.IntegrationKakaoRegisterReqDto;
import troublog.backend.domain.auth.dto.IntegrationRegisterReqDto;
import troublog.backend.domain.auth.dto.LoginReqDto;
import troublog.backend.domain.auth.dto.LoginResDto;
import troublog.backend.domain.auth.dto.OAuth2RegisterReqDto;
import troublog.backend.domain.auth.dto.RegisterReqDto;
import troublog.backend.domain.auth.dto.RegisterResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.facade.command.TermsCommandFacade;
import troublog.backend.domain.terms.service.query.TermsQueryService;
import troublog.backend.domain.terms.validator.TermsValidator;
import troublog.backend.domain.trouble.enums.LoginType;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.user.converter.UserConverter;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;
import troublog.backend.domain.user.service.command.UserCommandService;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.domain.user.validator.UserValidator;
import troublog.backend.global.common.constant.Domain;
import troublog.backend.global.common.constant.EnvType;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;
import troublog.backend.global.common.util.AlertSseUtil;
import troublog.backend.global.common.util.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthFacade {

	public static final String ENV_TYPE_HEADER = "EnvType";
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final PostQueryService postQueryService;
	private final TermsCommandFacade termsCommandFacade;
	private final TermsQueryService termsQueryService;
	private final UserValidator userValidator;

	private final AlertCommandService alertCommandService;

	private final AlertSseUtil alertSseUtil;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Value("${spring.profiles.active}")
	private String profilesActive;

	@Transactional
	public RegisterResDto register(RegisterReqDto registerReqDto, HttpServletRequest request) {

		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

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

		TermsValidator.validateForRegistration(registerReqDto.termsAgreements(), termsQueryService);
		User user = UserConverter.toEntity(registerReqDto, encodedPassword);
		userCommandService.save(user);

		TermsAgreementResDto termsAgreementResDto = termsCommandFacade.agreeToTerms(
			registerReqDto.termsAgreements(),
			user.getId()
		);

		return RegisterResDto.builder()
			.userId(user.getId())
			.termsAgreement(termsAgreementResDto)
			.build();
	}

	@Transactional
	public LoginResDto login(LoginReqDto loginReqDto, HttpServletRequest request, HttpServletResponse response) {

		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 유저 확인
		User user = userQueryService.findUserByEmailAndIsDeletedFalseAndStatusActive(loginReqDto.email());

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

		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// jwtProvider 에서 액세스토큰의 만료시간, 리프레시토큰의 유효성 검증
		Long userId = jwtProvider.reissueAccessToken(request);

		// 새로운 액세스토큰에 넣어 줄 유저 정보 조회
		User user = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(userId);

		//새로운 CustomAuthenticationToken 객체 생성
		CustomAuthenticationToken authenticationToken =
			CustomAuthenticationToken.unauthenticated(
				user.getEmail(),
				user.getPassword(),
				user.getId(),
				clientEnvType,
				user.getNickname());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		int writingCount = postQueryService.findPostByStatusAndUserId(user.getId(), PostStatus.WRITING).size();

		// 작성중인 트러블 슈팅 알림전송
		if (writingCount > 0) {

			// 환경에 맞게 url 세팅
			// ex) dev 환경 -> https://troublog.vercel.app/user/mypage/2
			String targetUrl = Domain.fromEnvType(EnvType.valueOfEnvType(clientEnvType)) + "/user/mypage/2";

			Alert alert = AlertConverter.postTroubleshootingAlert(user, writingCount, targetUrl);
			AlertResDto alertResDto = AlertConverter.convertToAlertResDto(alert);

			if (alertSseUtil.sendAlert(user.getId(), alertResDto)) {
				alert.markAsSent();
			}

			alertCommandService.save(alert);
		}

		// 새로운 액세스토큰 생성
		return jwtProvider.createAuthToken(authentication);
	}

	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		// 로그아웃
		jwtProvider.logout(request);

		// set-cookie 에서 삭제
		jwtProvider.deleteCookieRefreshToken(response);
	}

	@Transactional(readOnly = true)
	public void checkDuplicateEmail(String email, HttpServletRequest request) {

		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		if (!userQueryService.existsByEmail(email, UserStatus.ACTIVE)) {
			return;
		}

		User user = userQueryService.findUserByEmailAndIsDeletedFalseAndStatusActive(email);

		boolean isDuplicated = user.getEmail().equals(email);
		boolean isKaKaoRegistered =
			user.getIsIntegrated().equals(Boolean.FALSE) && user.getLoginType().equals(LoginType.KAKAO.getValue());

		// 이메일 중복
		if (isDuplicated) {
			// 이미 카카오로 회원가입됨 -> 통합 유도
			if (isKaKaoRegistered) {
				throw new UserException(ErrorCode.DUPLICATED_EMAIL_KAKAO);
			}
			// 일반회원가입으로 이미 가입되었거나 이미 통합된 계정중복
			throw new UserException(ErrorCode.DUPLICATED_EMAIL);
		}
	}

	@Transactional
	public RegisterResDto oAuthRegister(OAuth2RegisterReqDto oAuth2RegisterReqDto, HttpServletRequest request) {

		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		User user = userQueryService.findUserByIdAndIsDeletedFalseAndStatusActive(oAuth2RegisterReqDto.userId());

		// 닉네임 중복 체크
		boolean isDuplicatedNickname = false;

		// 카카오 닉네임과 임시저장된 유저 닉네임 비교
		if (oAuth2RegisterReqDto.kakaoNickname().equals(user.getNickname())) {
			// 유저가 입력한 닉네임의 중복여부 체크
			if (!oAuth2RegisterReqDto.nickname().equals(user.getNickname())) {
				isDuplicatedNickname = userQueryService.existsByNickname(oAuth2RegisterReqDto.nickname());
			}
		} else {
			throw new UserException(ErrorCode.USER_INVALID_NICKNAME);
		}

		if (isDuplicatedNickname) {
			throw new UserException(ErrorCode.DUPLICATED_NICKNAME);
		}

		user.updateOAuth2Info(oAuth2RegisterReqDto.nickname(), oAuth2RegisterReqDto.field(), oAuth2RegisterReqDto.bio(),
			oAuth2RegisterReqDto.githubUrl());

		TermsAgreementResDto termsAgreementResDto = termsCommandFacade.agreeToTerms(
			oAuth2RegisterReqDto.termsAgreements(),
			user.getId()
		);

		return RegisterResDto.builder()
			.userId(user.getId())
			.termsAgreement(termsAgreementResDto)
			.build();
	}

	@Transactional
	public void integrateUser(IntegrationRegisterReqDto integrationRegisterReqDto, HttpServletRequest request) {

		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		User user = userQueryService.findUserByEmailAndIsDeletedFalseAndStatusActive(integrationRegisterReqDto.email());

		// 통합된 유저로 변경
		user.updateIntegrateUser(passwordEncoder.encode(integrationRegisterReqDto.password()));
	}

	@Transactional
	public void integrateKakaoUser(IntegrationKakaoRegisterReqDto integrationKakaoRegisterReqDto,
		HttpServletRequest request) {

		String clientEnvType = request.getHeader(ENV_TYPE_HEADER);

		// 프론트 환경변수 체크
		jwtProvider.checkEnvType(clientEnvType);

		User user = userQueryService.findUserByEmailAndIsDeletedFalseAndStatusActive(
			integrationKakaoRegisterReqDto.email());

		// 비밀번호 검증
		userValidator.validateUserPassword(user, integrationKakaoRegisterReqDto.password());

		// 통합된 유저로 변경
		user.updateIntegrateKakaoUser(passwordEncoder.encode(integrationKakaoRegisterReqDto.password()),
			integrationKakaoRegisterReqDto.socialId(), integrationKakaoRegisterReqDto.proflImgUrl());
	}
}
