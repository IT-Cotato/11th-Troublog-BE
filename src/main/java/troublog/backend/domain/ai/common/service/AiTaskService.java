package troublog.backend.domain.ai.common.service;

import java.util.concurrent.CompletableFuture;

import troublog.backend.global.common.error.exception.AiTaskException;

public interface AiTaskService<I, O> {

	/**
	 * 동기적으로 AI 작업을 수행합니다.
	 *
	 * @param input 작업 입력 데이터
	 * @return 작업 결과
	 * @throws AiTaskException 작업 실행 중 오류 발생 시
	 */
	O execute(I input, I type);

	/**
	 * 비동기적으로 AI 작업을 수행합니다.
	 *
	 * @param input 작업 입력 데이터
	 * @return 작업 결과의 CompletableFuture
	 */
	CompletableFuture<O> executeAsync(I input, I type);

	/**
	 * 입력 데이터 유효성을 검증합니다.
	 *
	 * @param input 검증할 입력 데이터
	 * @return 유효한지 여부
	 */
	boolean validateInput(I input);
}