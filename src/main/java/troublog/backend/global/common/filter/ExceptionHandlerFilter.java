package troublog.backend.global.common.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter에서 일어나는 Exception을 처리하는 필터
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver exceptionResolver;

	public ExceptionHandlerFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.exceptionResolver=resolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("[ExceptionHandlerFilter] Filter 단계에서 예외 발생");
			exceptionResolver.resolveException(request, response, null, e);
		}
	}
}
