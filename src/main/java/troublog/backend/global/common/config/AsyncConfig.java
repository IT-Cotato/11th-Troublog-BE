package troublog.backend.global.common.config;

import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	private static final String SUMMARY_EXECUTOR_PREFIX = "AI-Summary";
	private static final String IMAGE_EXECUTOR_PREFIX = "Image";
	private static final String MAIL_EXECUTOR_PREFIX = "Mail";

	@Bean(name = "summaryExecutor")
	public Executor summaryExecutor() {
		return createVirtualThreadExecutor(SUMMARY_EXECUTOR_PREFIX);
	}

	@Bean(name = "imageExecutor")
	public Executor imageExecutor() {
		return createVirtualThreadExecutor(IMAGE_EXECUTOR_PREFIX);
	}

	@Bean(name = "mailExecutor")
	public Executor mailExecutor() {
		return createVirtualThreadExecutor(MAIL_EXECUTOR_PREFIX);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) ->
			log.error("[Async] 비동기 작업 중 예외 발생 - Method: {}, Params: {}", method.getName(), params, ex);
	}

	private Executor createVirtualThreadExecutor(String threadNamePrefix) {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setThreadNamePrefix(threadNamePrefix + "-");
		executor.setVirtualThreads(true);
		executor.setTaskDecorator(new MdcPropagatingTaskDecorator());

		log.info("[Async] {} Virtual Thread Executor 초기화 완료", threadNamePrefix);
		return executor;
	}

	private static class MdcPropagatingTaskDecorator implements TaskDecorator {

		@Override
		public Runnable decorate(Runnable runnable) {
			Map<String, String> contextMap = MDC.getCopyOfContextMap();

			return () -> {
				try {
					if (contextMap != null) {
						MDC.setContextMap(contextMap);
					}
					runnable.run();
				} finally {
					MDC.clear();
				}
			};
		}
	}
}
