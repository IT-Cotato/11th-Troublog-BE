package troublog.backend.global.common.filter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HexFormat;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.global.common.util.JwtProvider;

//* HTTP 요청의 추적 정보를 MDC에 설정하는 필터
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcFilter implements Filter {

	private static final String TRACE_ID_HEADER = "X-Trace-Id";
	private static final int ID_LENGTH = 16;

	//* MDC Keys
	private static final String TRACE_ID_KEY = "traceId";
	private static final String USER_ID_KEY = "userId";
	private static final String REQUEST_URI_KEY = "requestUri";
	private static final String HTTP_METHOD_KEY = "httpMethod";
	private static final String EXECUTION_TIME_KEY = "executionTime";
	public static final String ACTUATOR_URL_PREFIX = "/actuator";

	private final JwtProvider jwtProvider;
	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		long startTime = System.currentTimeMillis();

		try {
			setupMdcContext(httpRequest, httpResponse);
			MDC.put(EXECUTION_TIME_KEY, "PROCESSING");
			chain.doFilter(request, response);
		} finally {
			long executionTime = System.currentTimeMillis() - startTime;
			MDC.put(EXECUTION_TIME_KEY, executionTime + "ms");
			if (log.isInfoEnabled()) {
				log.info("Request completed : {} ms", executionTime);
			}
			MDC.clear();
		}
	}

	//* MDC 컨텍스트 설정
	private void setupMdcContext(final HttpServletRequest request, final HttpServletResponse response) {
		setupTrackingIds(request, response);
		setupRequestInfo(request);
		setupUserInfo(request);
	}

	//* Trace ID 설정
	private void setupTrackingIds(final HttpServletRequest request, final HttpServletResponse response) {
		String traceId = resolveOrGenerateId(request);
		MDC.put(TRACE_ID_KEY, traceId);
		response.setHeader(TRACE_ID_HEADER, traceId);
	}

	//* 요청 정보 설정
	private void setupRequestInfo(final HttpServletRequest request) {
		MDC.put(REQUEST_URI_KEY, request.getRequestURI());
		MDC.put(HTTP_METHOD_KEY, request.getMethod());
	}

	//* 사용자 정보 설정
	private void setupUserInfo(final HttpServletRequest request) {
		String userId = jwtProvider.extractUserIdFromRequest(request);
		if (StringUtils.hasText(userId)) {
			MDC.put(USER_ID_KEY, userId);
		}
	}

	//* 헤더에서 ID를 추출하거나 새로 생성
	private String resolveOrGenerateId(final HttpServletRequest request) {
		String existingId = request.getHeader(TRACE_ID_HEADER);
		return StringUtils.hasText(existingId) ? existingId.trim() : generateId();
	}

	//* 16자리 랜덤 ID 생성
	private String generateId() {
		byte[] bytes = new byte[ID_LENGTH / 2];
		secureRandom.nextBytes(bytes);
		return HexFormat.of().formatHex(bytes);
	}
}