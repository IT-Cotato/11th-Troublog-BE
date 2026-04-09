package troublog.backend.global.common.error.exception.handler;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.sentry.Sentry;

@Aspect
@Component
public class SentryExceptionCaptureAspect {

	@AfterReturning(
		pointcut = "within(troublog.backend.global.common.error.exception.handler.GlobalExceptionHandler)",
		returning = "response"
	)
	public void captureIfServerError(JoinPoint joinPoint, ResponseEntity<?> response) {
		if (!response.getStatusCode().is5xxServerError()) {
			return;
		}
		Arrays.stream(joinPoint.getArgs())
			.filter(Exception.class::isInstance)
			.findFirst()
			.ifPresent(ex -> Sentry.captureException((Exception) ex));
	}
}
