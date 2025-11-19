package troublog.backend.domain.auth.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.auth.entity.LoginType;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.domain.user.service.command.UserCommandService;
import troublog.backend.global.common.constant.Domain;
import troublog.backend.global.common.constant.EnvType;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.util.JwtProvider;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;

	public static final String OAUTH2_KAKAO = "KAKAO_";
	public static final String EMAIL_DOMAIN_TEMP = "_@social.temp";

	@Value("${spring.profiles.active}")
	private String profilesActive;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws
		IOException {

		log.info("OAuth2 Login 성공!");

		// Oauth 유저 객체 꺼내기
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		// 유저 객체에서 사용자 정보 가져오기
		Oauth2UserInfo oauth2UserInfo = getOauth2UserInfo(oAuth2User);

		// 유저 조회해오기, 없으면 신규 유저 생성
		User user = findOrCreateUserByEmail(oauth2UserInfo.nickname(), oauth2UserInfo.profileImageUrl(),
			oauth2UserInfo.socialId(),
			oauth2UserInfo.email());

		// Oauth로그인 시 클라이언트 envtype은 서버와 동일하게 지정
		String clientEnvType = String.valueOf(EnvType.valueOfEnvType(profilesActive));

		// 유저 객체 가져오기
		boolean isNewUser = user.getStatus() == UserStatus.INCOMPLETE;
		boolean isIntegrated = user.getIsIntegrated();

		if (isNewUser) {
			// 신규 유저: 프론트엔드 회원가입 완료 페이지로 리다이렉트
			handleNewUserRedirect(response, user);
		} else {
			if (isIntegrated) {
				// 기존 유저: 프론트엔드 메인 페이지로 토큰과 함께 리다이렉트
				handleExistingUserRedirect(response, user, clientEnvType);
			} else {
				// 통합이 되지 않은 유저: 통합 연동하기 페이지로 리다이렉트
				handleNotIntegratedUserRedirect(response, user, oauth2UserInfo.socialId(),
					oauth2UserInfo.profileImageUrl());
			}
		}
	}

	private Oauth2UserInfo getOauth2UserInfo(OAuth2User oAuth2User) {
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String socialId = String.valueOf(attributes.get("id"));

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		// 닉네임, 프로필사진, 이메일 가져오기
		String nickname = (String)profile.get("nickname");
		String profileImageUrl = (String)profile.get("profile_image_url");

		String email = extractEmail(kakaoAccount);
		return new Oauth2UserInfo(socialId, nickname, profileImageUrl, email);
	}

	private record Oauth2UserInfo(String socialId, String nickname, String profileImageUrl, String email) {
	}

	private String extractEmail(Map<String, Object> kakaoAccount) {
		if (kakaoAccount == null) {
			return null;
		}

		Boolean hasEmail = (Boolean)kakaoAccount.get("has_email");
		if (!Boolean.TRUE.equals(hasEmail)) {
			return null;
		}

		return (String)kakaoAccount.get("email");
	}

	private User findOrCreateUserByEmail(String nickname, String profileImageUrl, String socialId, String email) {
		return userQueryService.findUserByEmailAndIsDeletedFalseAndStatusActiveSocial(email)
			.orElseGet(() -> {
				// 닉네임 중복 체크 및 처리
				String uniqueNickname = generateUniqueNickname(nickname);
				String resolvedEmail = resolveEmail(email, socialId);

				log.info("신규 카카오 사용자 등록: socialId={}, originalNickname={}, uniqueNickname={}",
					socialId, nickname, uniqueNickname);

				return userCommandService.save(
					User.builder()
						.email(resolvedEmail)
						.nickname(uniqueNickname)
						.profileUrl(profileImageUrl)
						.status(UserStatus.INCOMPLETE) // 프로필 정보 미완성 상태
						.loginType(LoginType.KAKAO.getValue())
						.socialId(socialId)
						.password(passwordEncoder.encode(UUID.randomUUID().toString()))
						.isDeleted(false)
						.isIntegrated(false)
						// field, bio, githubUrl은 null로 두고 나중에 입력받음
						.build()
				);
			});
	}

	private String resolveEmail(String email, String socialId) {
		if (StringUtils.isNoneEmpty(email)) {
			return email;
		}

		return OAUTH2_KAKAO + socialId + EMAIL_DOMAIN_TEMP;
	}

	/**
	 * 닉네임 중복일 때 임의 채번해주는 로직 (에러가 뜨면 안되므로)
	 * TODO : 일반 로그인으로 가입한 유저가 카카오 로그인으로 인증할 때 연결해주는 로직 필요
	 */
	private String generateUniqueNickname(String originalNickname) {
		String nickname = originalNickname;
		int counter = 1;

		// 닉네임이 중복되면 숫자를 붙여서 유니크하게 만듦
		while (userQueryService.existsByNickname(nickname)) {
			nickname = originalNickname + "_" + counter;
			counter++;
		}

		return nickname;
	}

	private void handleNewUserRedirect(HttpServletResponse response, User user) throws
		IOException {
		log.info("신규 카카오 유저 회원가입 완료: userId={}", user.getId());

		// 프론트엔드 도메인 가져오기
		String frontendDomain = Domain.fromEnvType(EnvType.valueOfEnvType(profilesActive));

		// 신규 유저 정보를 URL 파라미터로 전달
		String redirectUrl = String.format("%s/auth/oauth-register?userId=%d&nickname=%s&loginType=%s&status=%s",
			frontendDomain,
			user.getId(),
			URLEncoder.encode(user.getNickname(), StandardCharsets.UTF_8),
			user.getLoginType(),
			user.getStatus().name()
		);
		response.sendRedirect(redirectUrl);
	}

	private void handleExistingUserRedirect(HttpServletResponse response, User user, String clientEnvType) throws
		IOException {
		log.info("통합이 이미 진행된 기존 카카오 유저 로그인: userId={}, email={}", user.getId(), user.getEmail());

		// CustomAuthenticationToken 생성 (일반 로그인과 동일)
		CustomAuthenticationToken authenticationToken = CustomAuthenticationToken.unauthenticated(
			user.getEmail(),
			user.getPassword(),
			user.getId(),
			clientEnvType,
			user.getNickname()
		);

		// 토큰 생성 (jwtProvider 사용)
		String accessToken = jwtProvider.createAuthToken(authenticationToken);
		String refreshToken = jwtProvider.createRefreshToken(authenticationToken);

		// 리프레시 토큰 Set-Cookie로 설정
		jwtProvider.setCookieRefreshToken(refreshToken, response);

		// 프론트엔드 도메인 가져오기
		String frontendDomain = Domain.fromEnvType(EnvType.valueOfEnvType(profilesActive));

		// 로그인 성공 시 토큰을 URL 파라미터로 전달하여 메인 페이지로 리다이렉트
		String redirectUrl = String.format("%s/user/home?userId=%d&accessToken=%s",
			frontendDomain,
			user.getId(),
			URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
		);
		response.sendRedirect(redirectUrl);
	}

	private void handleNotIntegratedUserRedirect(HttpServletResponse response, User user, String socialId,
		String profileImgUrl) throws
		IOException {

		log.info("통합 되지 않은 유저: userId={}", user.getId());

		// 프론트엔드 도메인 가져오기
		String frontendDomain = Domain.fromEnvType(EnvType.valueOfEnvType(profilesActive));

		// 신규 유저 정보를 URL 파라미터로 전달
		String redirectUrl = String.format("%s/auth/oauth-integration?userId=%d&socialId=%s&profileImgUrl=%s",
			frontendDomain,
			user.getId(),
			URLEncoder.encode(socialId, StandardCharsets.UTF_8),
			profileImgUrl != null ? URLEncoder.encode(profileImgUrl, StandardCharsets.UTF_8) : ""
		);
		response.sendRedirect(redirectUrl);
	}
}

