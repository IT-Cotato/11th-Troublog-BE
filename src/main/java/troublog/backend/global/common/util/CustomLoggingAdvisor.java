package troublog.backend.global.common.util;

import java.util.Optional;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * OpenAI 사용량 추적 및 디버깅을 위한 Advisor
 */
@Slf4j
@Component
public class CustomLoggingAdvisor implements CallAdvisor {

	private static final String AI_TAG = "[AI]";
	private static final String LOG_FORMAT = "{} {}";

	@Override
	public String getName() {
		return "SimpleLoggingAdvisor";
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
		ChatClientResponse advisedResponse;
		long start = System.currentTimeMillis();
		loggingRequest(request);
		try {
			advisedResponse = chain.nextCall(request);
		} finally {
			long tookMs = System.currentTimeMillis() - start;
			log.info(LOG_FORMAT, AI_TAG, String.format("Call duration: %d ms", tookMs));
		}

		long tookMs = System.currentTimeMillis() - start;
		log.info(LOG_FORMAT, AI_TAG, String.format("Call duration: %d ms", tookMs));
		log.info("============ Response ============");
		Optional.ofNullable(advisedResponse.chatResponse())
			.map(ChatResponse::getMetadata)
			.map(ChatResponseMetadata::getUsage)
			.ifPresent(usage -> {
				log.info(LOG_FORMAT, AI_TAG, String.format("Input Tokens: %d", usage.getPromptTokens()));
				log.info(LOG_FORMAT, AI_TAG, String.format("Output Tokens: %d", usage.getCompletionTokens()));
				log.info(LOG_FORMAT, AI_TAG, String.format("Total Tokens: %d", usage.getTotalTokens()));
			});

		log.info("==================================");
		return advisedResponse;
	}

	private static void loggingRequest(ChatClientRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("============== Request =============");
			String contents = Optional.of(request.prompt())
				.map(p -> String.valueOf(p.getContents()))
				.orElse("(null)");
			log.debug(LOG_FORMAT, AI_TAG, "Prompt: " + contents.replaceAll("(?i)sk-[A-Za-z0-9]{10,}", "***"));

		}
	}
}