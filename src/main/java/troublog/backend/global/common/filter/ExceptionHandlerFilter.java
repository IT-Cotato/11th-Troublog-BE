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

	/**
	 * Constructs an ExceptionHandlerFilter with the specified HandlerExceptionResolver.
	 *
	 * @param resolver the HandlerExceptionResolver used to process exceptions caught during filter execution
	 */
	public ExceptionHandlerFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.exceptionResolver=resolver;
	}

	/**
	 * Processes the HTTP request and response through the filter chain, handling any exceptions by delegating to the configured exception resolver.
	 *
	 * If an exception occurs during filter chain execution, it is caught and passed to the injected {@link HandlerExceptionResolver} for centralized handling.
	 *
	 * @param request the current HTTP request
	 * @param response the current HTTP response
	 * @param filterChain the filter chain to execute
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs during filtering
	 */
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
