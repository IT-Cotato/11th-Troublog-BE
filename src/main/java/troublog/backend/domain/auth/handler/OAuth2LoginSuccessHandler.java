package troublog.backend.domain.auth.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.auth.dto.LoginResDto;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.domain.user.service.command.UserCommandService;
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
	private final ObjectMapper objectMapper;
	private final PasswordEncoder passwordEncoder;

	@Value("${spring.profiles.active}")
	private String profilesActive;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws
		IOException, ServletException {

		log.info("OAuth2 Login 성공!");
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		String clientEnvType = String.valueOf(EnvType.valueOfEnvType(profilesActive));

		User user = processOAuth2User(oAuth2User);
		boolean isNewUser = user.getStatus() == UserStatus.INCOMPLETE;

		if (isNewUser) {
			// 신규 유저: 회원가입 데이터 반환 (일반 JSON 응답)
			handleNewUserResponse(response, user);
		} else {
			// 기존 유저: 로그인 로직과 동일하게 토큰 반환
			handleExistingUserLogin(response, user, clientEnvType);
		}
	}

	private User processOAuth2User(OAuth2User oAuth2User) {
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String socialId = String.valueOf(attributes.get("id"));

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		String nickname = (String)profile.get("nickname");
		String profileImageUrl = (String)profile.get("profile_image_url");

		return findOrCreateUserBySocialId(nickname, profileImageUrl, socialId);
	}

	private User findOrCreateUserBySocialId(String nickname, String profileImageUrl, String socialId) {
		return userQueryService.findUserBySocialId(socialId)
			.orElseGet(() -> {
				// 닉네임 중복 체크 및 처리
				String uniqueNickname = generateUniqueNickname(nickname);
				
				log.info("신규 카카오 사용자 등록: socialId={}, originalNickname={}, uniqueNickname={}", 
					socialId, nickname, uniqueNickname);
				
				return userCommandService.save(
					User.builder()
						.email("kakao_" + socialId + "@social.temp") // 카카오 socialId 기반 임시 이메일
						.nickname(uniqueNickname)
						.profileUrl(profileImageUrl)
						.status(UserStatus.INCOMPLETE) // 프로필 정보 미완성 상태
						.loginType("KAKAO")
						.socialId(socialId)
						.password(passwordEncoder.encode(UUID.randomUUID().toString()))
						.isDeleted(false)
						// field, bio, githubUrl은 null로 두고 나중에 입력받음
						.build()
				);
			});
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


	private void handleNewUserResponse(HttpServletResponse response, User user) throws IOException {
		log.info("신규 카카오 유저 회원가입 완료: userId={}", user.getId());
		
		// 신규 유저 데이터 응답
		Map<String, Object> userResponse = Map.of(
			"id", user.getId(),
			"email", user.getEmail(),
			"nickname", user.getNickname(),
			"loginType", user.getLoginType(),
			"status", user.getStatus().name()
		);

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(objectMapper.writeValueAsString(userResponse));
	}

	private void handleExistingUserLogin(HttpServletResponse response, User user, String clientEnvType) throws IOException {
		log.info("기존 카카오 유저 로그인: userId={}", user.getId());

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
		String localToken = jwtProvider.createAuthToken(authenticationToken);
		String refreshToken = jwtProvider.createRefreshToken(authenticationToken);

		// 리프레시 토큰 Set-Cookie로 설정
		jwtProvider.setCookieRefreshToken(refreshToken, response);

		// 로그인 응답 생성 (일반 로그인과 동일)
		LoginResDto loginResDto = (profilesActive.equals(EnvType.LOCAL.getEnvType()) || clientEnvType.equals(EnvType.LOCAL.getEnvType()))
			? LoginResDto.localReturn(user.getId(), accessToken, refreshToken, localToken)
			: LoginResDto.nonLocalReturn(user.getId(), accessToken, refreshToken);

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(objectMapper.writeValueAsString(loginResDto));
	}
}


