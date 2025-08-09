package troublog.backend.domain.auth.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.auth.service.AuthFacade;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.entity.UserStatus;
import troublog.backend.global.common.util.JwtProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final AuthFacade authFacade;
	private final JwtProvider jwtProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws
		IOException, ServletException {

		log.info("OAuth2 Login 성공!");
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		User user = processOAuth2User(oAuth2User);

		String accessToken = jwtProvider.createAuthToken(authentication);
		String refreshToken = jwtProvider.createRefreshToken(authentication);

		authFacade.saveRefreshToken(user, refreshToken);

		boolean isNewUser = user.getStatus() == UserStatus.INCOMPLETE;
		String targetUrl = createTargetUrl(accessToken, refreshToken, isNewUser);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private User processOAuth2User(OAuth2User oAuth2User) {
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String socialId = String.valueOf(attributes.get("id"));

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		String nickname = (String)profile.get("nickname");
		String profileImageUrl = (String)profile.get("profile_image_url");

		return authFacade.findOrCreateUserBySocialId(nickname, profileImageUrl, socialId);
	}

	private String createTargetUrl(String accessToken, String refreshToken, boolean isNewUser) {

		// TODO: 프론트엔드와 협의하여 리다이렉트 URL을 결정해야 합니다.
		String targetUrl = "http://localhost:5173/oauth-redirect"; // 예시 URL

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("accessToken", accessToken)
			.queryParam("refreshToken", refreshToken)
			.queryParam("isNewUser", isNewUser)
			.build().toUriString();
	}
}


