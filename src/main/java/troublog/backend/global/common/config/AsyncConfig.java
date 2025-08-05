package troublog.backend.global.common.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	private static final String IMAGE_PREFIX = "Image";
	public static final String SUMMARY_PREFIX = "AI-Summary";

	@Bean(name = "summaryExecutor")
	public Executor summaryExecutor() {
		return createVirtualThreadExecutor(SUMMARY_PREFIX);
	}

	@Bean(name = "imageExecutor")
	public Executor imageExecutor() {
		return createVirtualThreadExecutor(IMAGE_PREFIX);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) ->
			log.error("[Async] 비동기 작업중 예외 발생 - Method: {}, Params: {}",
				method.getName(), params, ex);
	}

	private Executor createVirtualThreadExecutor(String threadNamePrefix) {
		Executor virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
		Executor decoratedExecutor = new ContextAwareVirtualThreadExecutor(
			virtualExecutor, threadNamePrefix);

		log.info("{} Virtual Thread Executor 초기화 완료", threadNamePrefix);
		return decoratedExecutor;
	}

	private record ContextAwareVirtualThreadExecutor(Executor delegate, String threadNamePrefix) implements Executor {

		@Override
		public void execute(@NonNull Runnable command) {
			Runnable contextAwareTask = decorateWithContext(command);
			Runnable namedTask = decorateWithThreadName(contextAwareTask);
			delegate.execute(namedTask);
		}

		private Runnable decorateWithContext(Runnable task) {
			try {
				RequestAttributes context = RequestContextHolder.currentRequestAttributes();
				return () -> {
					try {
						RequestContextHolder.setRequestAttributes(context);
						task.run();
					} finally {
						RequestContextHolder.resetRequestAttributes();
					}
				};
			} catch (IllegalStateException e) {
				return task;
			}
		}

		private Runnable decorateWithThreadName(Runnable task) {
			return () -> {
				Thread currentThread = Thread.currentThread();
				String originalName = currentThread.getName();
				try {
					currentThread.setName(threadNamePrefix + "-" + currentThread.threadId());
					task.run();
				} finally {
					currentThread.setName(originalName);
				}
			};
		}
	}
}