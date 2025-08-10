package troublog.backend.global.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import io.micrometer.observation.ObservationRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.config.property.OpenAiProperties;
import troublog.backend.global.common.util.CustomLoggingAdvisor;

/**
 * Spring AI 및 OpenAI ChatClient 설정을 담당하는 구성 클래스
 * 주요 기능:
 * - OpenAI API 클라이언트 설정
 * - ChatClient 빈 생성 및 Advisor 구성
 * - 메모리 기반 대화 관리 설정
 */
@Configuration
@EnableConfigurationProperties({PromptProperties.class})
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AiConfig {

	private final ToolCallingManager toolCallingManager;
	private final CustomLoggingAdvisor customLoggingAdvisor;
	private final OpenAiProperties openAiProperties;
	private final RetryTemplate retryTemplate;

	/**
	 * 대화 메모리를 관리하는 Advisor를 생성합니다.
	 * MessageWindowChatMemory를 사용하여 최근 대화 이력을 유지합니다.
	 *
	 * @return MessageChatMemoryAdvisor 인스턴스
	 */
	@Bean
	public MessageChatMemoryAdvisor chatMemoryAdvisor() {
		MessageWindowChatMemory chatMemory = createMessageWindowChatMemory();
		return MessageChatMemoryAdvisor.builder(chatMemory)
			.build();
	}

	/**
	 * OpenAI ChatClient를 구성하고 생성합니다.
	 * 로깅과 메모리 관리 기능을 포함한 ChatClient를 반환합니다.
	 *
	 * @return 완전히 구성된 ChatClient 인스턴스
	 */
	@Bean
	public ChatClient chatClient() {
		OpenAiApi openAiApi = createOpenAiApi();
		OpenAiChatOptions chatOptions = createChatOptions();
		ChatModel chatModel = createChatModel(openAiApi, chatOptions);

		return ChatClient.builder(chatModel)
			.defaultAdvisors(customLoggingAdvisor, chatMemoryAdvisor())
			.build();
	}

	/**
	 * OpenAI API 클라이언트를 생성합니다.
	 *
	 * @return 구성된 OpenAiApi 인스턴스
	 */
	private OpenAiApi createOpenAiApi() {
		return OpenAiApi.builder()
			.apiKey(openAiProperties.apiKey())
			.build();
	}

	/**
	 * OpenAI 채팅 옵션을 생성합니다.
	 *
	 * @return 구성된 OpenAiChatOptions 인스턴스
	 */
	private OpenAiChatOptions createChatOptions() {
		return OpenAiChatOptions.builder()
			.model(openAiProperties.chat().options().model())
			.build();
	}

	/**
	 * OpenAI ChatModel을 생성합니다.
	 *
	 * @param openAiApi OpenAI API 클라이언트
	 * @param chatOptions 채팅 옵션
	 * @return 구성된 ChatModel 인스턴스
	 */
	private ChatModel createChatModel(OpenAiApi openAiApi, OpenAiChatOptions chatOptions) {
		return OpenAiChatModel.builder()
			.openAiApi(openAiApi)
			.defaultOptions(chatOptions)
			.toolCallingManager(toolCallingManager)
			.observationRegistry(createObservationRegistry())
			.retryTemplate(retryTemplate)
			.build();
	}

	private MessageWindowChatMemory createMessageWindowChatMemory() {
		return MessageWindowChatMemory.builder()
			.build();
	}

	private ObservationRegistry createObservationRegistry() {
		return ObservationRegistry.create();
	}
}