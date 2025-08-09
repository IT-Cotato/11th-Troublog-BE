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
import troublog.backend.global.common.constant.EnvType;
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

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

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

    public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInSeconds * 1000);

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;

        User user = User.builder()
            .id(customAuthenticationToken.getUserId())
            .build();

		// TODO: Refresh Token 저장 로직 (Redis? DB?) 현재는 DB
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

    public Long reissueAccessToken(HttpServletRequest request) {

        String accessToken = DataUtil.getValueFromRequest(request, AUTHORIZATION);

        // TODO : 임시로 헤더에서 받음
        // String clientRefreshToken = DataUtil.getValueFromRequest(request, "refreshToken");

        // TODO : 개발단계에서 사용
        String clientRefreshToken = extractRefreshTokenFromCookie(request);

        // 액세스토큰이 만료되었는지 확인
        isExpired(accessToken);

        // 리프레시토큰이 유효한지 확인 (만료검사 X)
        validateToken(clientRefreshToken);

        Claims accessTokenClaims = getClaimsWithoutExp(accessToken);
        Claims refreshTokenClaims = getClaimsWithoutExp(clientRefreshToken);

        Long userId = accessTokenClaims.get("userId", Long.class);
        Long refreshTokenId = refreshTokenClaims.get("refreshTokenId", Long.class);

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));

        // 리프레시 토큰이 만료된 경우 토큰 만료 처리 후 에러 리턴
        if(refreshToken.getExpiredAt().toInstant().isBefore(Instant.now())) {
            refreshToken.revokeRefreshToken();
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }

        // 토큰이 철회되거나 다른 유저의 토큰으로 접근하는 경우 에러 리턴
        if(refreshToken.isRevoked() || !refreshToken.getUser().getId().equals(userId)) {
           throw new AuthException(ErrorCode.INVALID_TOKEN);
        }

        return userId;
    }

    /**
     * 토큰을 받아 Authentication 객체를 반환
     * @param accessToken
     * @return
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
     * 토큰의 유효성을 검증
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
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
     * 토큰에서 Claims 정보를 추출
     * @param accessToken
     * @return
     */
    private Claims parseClaims(String accessToken) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(accessToken)
            .getPayload();
    }

    /**
     * 토큰에서 Claims 정보를 추출 (만료여부 체크하지 않는 로직)
     * @param accessToken
     * @return
     */
    private Claims getClaimsWithoutExp(String accessToken) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료됐더라도 payload는 추출 가능
            return e.getClaims();
        }
    }

    public boolean isNotExpired(String token) {
        try{
            parseClaims(token);
            return true;
        } catch(ExpiredJwtException e) {
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    private boolean isExpired(String token) {
        try {
            parseClaims(token);
            throw new AuthException(ErrorCode.TOKEN_NOT_EXPIRED);
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 토큰에서 만료일시 추출
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key) // 서명 검증
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return claims.getExpiration();
    }

    public void checkEnvType(String clientEnvType) {

        EnvType serverEnvType = EnvType.valueOfEnvType(profilesActive);
        EnvType frontEnvType = EnvType.valueOfEnvType(clientEnvType);

        if(serverEnvType != null &&
            !serverEnvType.isLocal() &&
            !(frontEnvType.isLocal() && serverEnvType.isDev()) &&
            !EnvType.isEqualEnvType(serverEnvType, frontEnvType)) {

            throw new AuthException(ErrorCode.WRONG_ENVIRONMENT);
        }
    }

    // TODO : 실제 운영 시 사용
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

    public void logout(HttpServletRequest request) {

        // TODO : 임시로 헤더에서 받음
        // String clientRefreshToken = DataUtil.getValueFromRequest(request, "refreshToken");

        // TODO : 개발단계에서 사용
        String clientRefreshToken = extractRefreshTokenFromCookie(request);

        Claims claims = parseClaims(clientRefreshToken);
        Long refreshTokenId = claims.get("refreshTokenId", Long.class);

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));

        // 리프레시토큰 철회
        refreshToken.revokeRefreshToken();
    }
}