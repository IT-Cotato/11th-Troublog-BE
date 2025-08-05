package troublog.backend.global.common.util;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.stereotype.Component;

import io.micrometer.common.lang.Nullable;

@Component
public class SimpleLoggingAdvisor implements CallAdvisor {

	private static final Logger logger = LoggerFactory.getLogger(SimpleLoggingAdvisor.class);
	private static final int DEFAULT_ORDER = 0;
	public static final Function<ChatClientRequest, String> DEFAULT_REQUEST_TO_STRING = ChatClientRequest::toString;
	public static final Function<ChatResponse, String> DEFAULT_RESPONSE_TO_STRING = ModelOptionsUtils::toJsonStringPrettyPrinter;

	private final Function<ChatClientRequest, String> requestToString;
	private final Function<ChatResponse, String> responseToString;
	private final int order;

	public SimpleLoggingAdvisor() {
		this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, DEFAULT_ORDER);
	}

	public SimpleLoggingAdvisor(int order) {
		this(DEFAULT_REQUEST_TO_STRING, DEFAULT_RESPONSE_TO_STRING, order);
	}

	public SimpleLoggingAdvisor(@Nullable Function<ChatClientRequest, String> requestToString,
		@Nullable Function<ChatResponse, String> responseToString, int order) {
		this.requestToString = getOrDefault(requestToString, DEFAULT_REQUEST_TO_STRING);
		this.responseToString = getOrDefault(responseToString, DEFAULT_RESPONSE_TO_STRING);
		this.order = order;
	}

	/**
	 * ChatClient 호출을 가로채서 요청과 응답을 로깅합니다.
	 *
	 * @param chatClientRequest 클라이언트 요청
	 * @param callAdvisorChain  advisor 체인
	 * @return 처리된 응답
	 */
	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
		logRequest(chatClientRequest);
		ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
		logResponse(chatClientResponse);
		return chatClientResponse;
	}

	/**
	 * 요청 정보를 디버그 레벨로 로깅합니다.
	 *
	 * @param request 로깅할 요청 객체
	 */
	private void logRequest(ChatClientRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("ChatClient Request: {}", requestToString.apply(request));
		}
	}

	/**
	 * 응답 정보를 디버그 레벨로 로깅합니다.
	 *
	 * @param chatClientResponse 로깅할 응답 객체
	 */
	private void logResponse(ChatClientResponse chatClientResponse) {
		if (logger.isDebugEnabled()) {
			logger.debug("ChatClient Response: {}", responseToString.apply(chatClientResponse.chatResponse()));
		}
	}

	private static <T> T getOrDefault(@Nullable T value, T defaultValue) {
		return value != null ? value : defaultValue;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return String.format("%s{order=%d}", getClass().getSimpleName(), order);
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * SimpleLoggingAdvisor를 위한 Builder 클래스
	 * 유연한 인스턴스 생성을 위한 빌더 패턴 구현
	 */
	public static final class Builder {
		private Function<ChatClientRequest, String> requestToString;
		private Function<ChatResponse, String> responseToString;
		private int order = DEFAULT_ORDER;

		private Builder() {
			// 외부에서 직접 인스턴스화 방지
		}

		/**
		 * 요청 객체를 문자열로 변환하는 함수를 설정합니다.
		 *
		 * @param requestToString 요청 변환 함수
		 * @return Builder 인스턴스 (메서드 체이닝용)
		 */
		public Builder requestToString(Function<ChatClientRequest, String> requestToString) {
			this.requestToString = requestToString;
			return this;
		}

		/**
		 * 응답 객체를 문자열로 변환하는 함수를 설정합니다.
		 *
		 * @param responseToString 응답 변환 함수
		 * @return Builder 인스턴스 (메서드 체이닝용)
		 */
		public Builder responseToString(Function<ChatResponse, String> responseToString) {
			this.responseToString = responseToString;
			return this;
		}

		/**
		 * advisor 실행 순서를 설정합니다.
		 *
		 * @param order 실행 순서 (낮은 숫자가 먼저 실행됨)
		 * @return Builder 인스턴스 (메서드 체이닝용)
		 */
		public Builder order(int order) {
			this.order = order;
			return this;
		}

		public SimpleLoggingAdvisor build() {
			return new SimpleLoggingAdvisor(requestToString, responseToString, order);
		}
	}

}