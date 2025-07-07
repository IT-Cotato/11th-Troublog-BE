package troublog.backend.global.common.util;

import static org.springframework.http.HttpHeaders.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.SecretKey;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import troublog.backend.domain.auth.entity.RefreshToken;
import troublog.backend.domain.auth.repository.RefreshTokenRepository;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AuthException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

	private static final long ENDLESS_TIME = 999999999; // local 개발 용 토큰 무한 사용 (11574일)
    private SecretKey key;

    /**
     * Initializes the secret key for JWT signing after dependency injection is complete.
     *
     * Generates an HMAC SHA key from the configured secret string for use in token operations.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT access token for the given authentication, using an endless expiration for local environments and a configured expiration for others.
     *
     * @param authentication the authentication object containing user details and environment type
     * @return the generated JWT access token as a string
     */
    public String createAuthToken(Authentication authentication) {
        String envType = (authentication instanceof CustomAuthenticationToken cat)
            ? cat.getEnvType()
            : null;

        // 로컬에서 서버 작업하거나 로컬에서 프론트 작업할 경우
        if("local".equals(profilesActive) || "local".equals(envType)) {
            return createToken(authentication, ENDLESS_TIME);
        } else {
            return createToken(authentication, accessTokenValidityInSeconds);
        }
    }

    /**
     * Generates a JWT access token containing user ID, environment type, and nickname claims, signed with the configured secret key and set to expire after the specified duration.
     *
     * @param authentication the authentication object containing user details
     * @param expireTime the token's validity period in seconds
     * @return a signed JWT access token as a string
     */
    private String createToken(Authentication authentication, long expireTime) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + expireTime * 1000);

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;

        return Jwts.builder()
            .subject(customAuthenticationToken.getName())
            .claim("userId", customAuthenticationToken.getUserId())
            .claim("EnvType", customAuthenticationToken.getEnvType())
            .claim("nickname", customAuthenticationToken.getNickname())
            .issuedAt(new Date())
            .expiration(validity)
            .signWith(key)
            .compact();
    }

    /**
     * Creates and persists a refresh token for the given authentication, returning a signed JWT containing the refresh token ID and environment type.
     *
     * The refresh token is stored in the repository and associated with the user. The resulting JWT includes claims for the environment type and the refresh token's unique identifier.
     *
     * @param authentication the authentication object representing the user for whom the refresh token is created
     * @return a signed JWT string representing the refresh token
     */
    public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInSeconds * 1000);

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;

        User user = User.builder()
            .id(customAuthenticationToken.getUserId())
            .build();

		// TODO: Refresh Token 저장 로직 (Redis? DB?)
        RefreshToken refreshToken = RefreshToken.of(user, validity);

        Long refreshTokenid = refreshTokenRepository.save(refreshToken).getId();

        return Jwts.builder()
            .subject(authentication.getName())
            .claim("EnvType", customAuthenticationToken.getEnvType())
            .claim("refreshTokenId", refreshTokenid)
            .issuedAt(new Date())
            .expiration(validity)
            .signWith(key)
            .compact();
    }

    /**
     * Validates the provided access and refresh tokens from the HTTP request and returns the user ID if the tokens are valid and not revoked.
     *
     * This method checks that the access token is expired, validates the refresh token, parses claims from both tokens, verifies the refresh token's existence and expiration, and ensures the token belongs to the correct user and is not revoked. Throws an {@code AuthException} with an appropriate error code if any validation fails.
     *
     * @param request the HTTP request containing the access and refresh tokens
     * @return the user ID associated with the validated tokens
     */
    public Long reissueAccessToken(HttpServletRequest request) {

        String accessToken = DataUtil.getValueFromRequest(request, AUTHORIZATION);

        // TODO : 임시로 헤더에서 받음
        String clientRefreshToken = DataUtil.getValueFromRequest(request, "refreshToken");

        // TODO : 개발단계에서 사용
        // String clientRefreshToken = extractRefreshTokenFromCookie(request);

        // 액세스토큰이 만료되었는지 확인
        isExpired(accessToken);

        // 리프레시토큰이 유효한지 확인 (만료검사 X)
        validateToken(clientRefreshToken);

        Claims accessTokenClaims = parseClaims(accessToken);
        Claims refreshTokenClaims = parseClaims(clientRefreshToken);

        Long userId = accessTokenClaims.get("userId", Long.class);
        Long refreshTokenId = refreshTokenClaims.get("refreshTokenId", Long.class);

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));

        // 리프레시 토큰이 만료된 경우 토큰 만료 처리 후 에러 리턴
        if(refreshToken.getExpiredAt().toInstant().isAfter(Instant.now())) {
            refreshToken.revokeRefreshToken();
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }

        // 토큰이 철회되거나 다른 유저의 토큰으로 접근하는 경우 에러 리턴
        if(refreshToken.isRevoked() && !refreshToken.getUser().getId().equals(userId)) {
           throw new AuthException(ErrorCode.INVALID_TOKEN);
        }

        return userId;
    }

    /**
     * Extracts authentication details from a JWT access token and returns a populated Authentication object.
     *
     * The returned Authentication contains the username, user ID, environment type, and nickname as parsed from the token claims.
     *
     * @param accessToken the JWT access token to parse
     * @return an Authentication object representing the user described by the token
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        String username = claims.getSubject(); // subject에서 사용자 로그인id 가져옴

        Long userId = claims.get("userId", Long.class);
        String clientEnvType = claims.get("EnvType", String.class);
        String nickname = claims.get("nickname", String.class);

        return CustomAuthenticationToken.authenticated(username, null, userId, clientEnvType, nickname);
    }

    /**
	 * Validates the structure and signature of a JWT token.
	 *
	 * @param token the JWT token to validate
	 * @return true if the token is valid; otherwise, throws an AuthException for invalid signature or token structure
	 */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SignatureException e) {
            throw new AuthException(ErrorCode.INVALID_SIGNATURE);
		} catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			// TODO : 토큰 구조 이상 or 지원하지 않는 형식의 토큰 or 토큰이 Null인 경우 -> 보안에 직접 영향 X -> warn
			if (e instanceof MalformedJwtException) {
				// 직접 토큰 출력해서 확인
				log.warn("{} tokenString={}", e.getLocalizedMessage(), token);
			}
			throw new AuthException(ErrorCode.INVALID_TOKEN);
		}
	}

    /**
     * Extracts and returns the claims from the provided JWT access token.
     *
     * @param accessToken the JWT access token to parse
     * @return the claims contained within the token
     */
    private Claims parseClaims(String accessToken) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(accessToken)
            .getPayload();
    }

    /**
     * Checks whether the provided JWT token is not expired.
     *
     * @param token the JWT token to check
     * @return true if the token is not expired
     * @throws AuthException if the token is expired
     */
    public boolean isNotExpired(String token) {
        try{
            parseClaims(token);
            return true;
        } catch(ExpiredJwtException e) {
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    /**
     * Checks if the provided JWT token is expired.
     *
     * @param token the JWT token to check
     * @return true if the token is expired
     * @throws AuthException if the token is not expired
     */
    private boolean isExpired(String token) {
        try {
            parseClaims(token);
            throw new AuthException(ErrorCode.TOKEN_NOT_EXPIRED);
        } catch (ExpiredJwtException e) {
            return true;
        }

    }

    /**
     * Extracts the refresh token value from the cookies in the given HTTP request.
     *
     * @param request the HTTP request containing cookies
     * @return the value of the "refreshToken" cookie
     * @throws AuthException if the "refreshToken" cookie is not found
     */
    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new AuthException(ErrorCode.TOKEN_NOT_FOUND);
        }

        return Arrays.stream(request.getCookies())
            .filter(cookie -> "refreshToken".equals(cookie.getName()))
            .findFirst()
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND))
            .getValue();
    }

    /**
     * Revokes the refresh token associated with the current user, effectively logging the user out.
     *
     * Extracts the refresh token from the request, parses its claims to identify the token entity,
     * and marks the corresponding refresh token as revoked in the repository.
     *
     * @param request the HTTP request containing the refresh token
     * @throws AuthException if the refresh token is not found or invalid
     */
    public void logout(HttpServletRequest request) {

        // TODO : 임시로 헤더에서 받음
        String clientRefreshToken = DataUtil.getValueFromRequest(request, "refreshToken");

        // TODO : 개발단계에서 사용
        // String clientRefreshToken = extractRefreshTokenFromCookie(request);

        Claims claims = parseClaims(clientRefreshToken);
        Long refreshTokenId = claims.get("refreshTokenId", Long.class);

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));

        // 리프레시토큰 철회
        refreshToken.revokeRefreshToken();
    }
}