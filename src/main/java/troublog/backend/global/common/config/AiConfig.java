package troublog.backend.global.common.config;

import java.util.Objects;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import com.google.genai.Client;

import io.micrometer.observation.ObservationRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.global.common.config.property.AnthropicProperties;
import troublog.backend.global.common.config.property.GeminiProperties;
import troublog.backend.global.common.config.property.OpenAiProperties;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.AiTaskException;
import troublog.backend.global.common.util.CustomLoggingAdvisor;

@Slf4j
@Configuration
@EnableConfigurationProperties({
	PromptProperties.class,
	OpenAiProperties.class,
	AnthropicProperties.class,
	GeminiProperties.class
})
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AiConfig {

	public static final String OPEN_AI = "openai";
	public static final String ANTHROPIC = "claude";
	public static final String GOOGLE_GEN_AI = "gemini";
	public static final String NOT_DEFIEND = "unknown";
	private final ToolCallingManager toolCallingManager;
	private final CustomLoggingAdvisor customLoggingAdvisor;
	private final OpenAiProperties openAiProperties;
	private final AnthropicProperties anthropicProperties;
	private final GeminiProperties geminiProperties;
	private final RetryTemplate retryTemplate;

	/**
	 * 기본 프로바이더 설정 (application.yml)
	 * ai.provider.default: openai | claude
	 */
	@Value("${ai.provider.default}")
	private String defaultProvider;

	/**
	 * 기본 ChatClient 빈 생성
	 * 설정된 프로바이더에 따라 OpenAI 또는 Claude 모델 사용
	 *
	 * @return 완전히 구성된 ChatClient 인스턴스
	 */
	@Bean
	public ChatClient chatClient() {
		ChatModel chatModel = createChatModelByProvider(defaultProvider);

		log.info("[AI] ChatClient 생성 완료 - Provider: {}, Model: {}",
			defaultProvider, getModelName(defaultProvider));

		return ChatClient.builder(chatModel)
			.defaultAdvisors(customLoggingAdvisor)
			.build();
	}

	/**
	 * 프로바이더에 따라 적절한 ChatModel 생성
	 *
	 * @param provider 프로바이더 이름 ("openai" 또는 "claude")
	 * @return ChatModel 인스턴스
	 */
	private ChatModel createChatModelByProvider(String provider) {
		log.info("[AI] ChatModel 생성 시작 - Provider: {}", provider);

		return switch (provider.toLowerCase()) {
			case OPEN_AI -> {
				log.info("[AI] OpenAI ChatModel 생성");
				yield createOpenAiChatModel();
			}
			case ANTHROPIC -> {
				log.info("[AI] Claude ChatModel 생성");
				yield createAnthropicChatModel();
			}
			case GOOGLE_GEN_AI -> {
				log.info("[AI] Gemini ChatModel 생성");
				yield createGeminiChatModel();
			}
			default -> throw new AiTaskException(ErrorCode.CHAT_MODEL_NOT_FOUND);
		};
	}


	/**
	 * Gemini ChatModel 생성
	 */
	private ChatModel createGeminiChatModel() {
		Client client = createGeminiClient();
		GoogleGenAiChatOptions chatOptions = createGeminiChatOption();

		return GoogleGenAiChatModel.builder()
			.genAiClient(client)
			.defaultOptions(chatOptions)
			.toolCallingManager(toolCallingManager)
			.observationRegistry(createObservationRegistry())
			.retryTemplate(retryTemplate)
			.build();
	}

	/**
	 * Gemini API 클라이언트 생성
	 */
	private Client createGeminiClient() {
		return Client.builder()
			.apiKey(geminiProperties.apiKey())
			.build();
	}

	/**
	 * Gemini 채팅 옵션 생성
	 */
	private GoogleGenAiChatOptions createGeminiChatOption() {
		return GoogleGenAiChatOptions.builder()
			.model(geminiProperties.chat().options().model())
			.maxOutputTokens(10000)
			.build();
	}

	/**
	 * OpenAI ChatModel 생성
	 */
	private ChatModel createOpenAiChatModel() {
		OpenAiApi openAiApi = createOpenAiApi();
		OpenAiChatOptions chatOptions = createOpenAiChatOptions();

		return OpenAiChatModel.builder()
			.openAiApi(openAiApi)
			.defaultOptions(chatOptions)
			.toolCallingManager(toolCallingManager)
			.observationRegistry(createObservationRegistry())
			.retryTemplate(retryTemplate)
			.build();
	}

	/**
	 * OpenAI API 클라이언트 생성
	 */
	private OpenAiApi createOpenAiApi() {
		return OpenAiApi.builder()
			.apiKey(Objects.requireNonNull(openAiProperties.apiKey()))
			.build();
	}

	/**
	 * OpenAI 채팅 옵션 생성
	 */
	private OpenAiChatOptions createOpenAiChatOptions() {
		String model = openAiProperties.chat().options().model();
		return OpenAiChatOptions.builder()
			.model(model)
			.maxTokens(10000)
			.build();
	}

	/**
	 * Anthropic ChatModel 생성
	 */
	private ChatModel createAnthropicChatModel() {
		AnthropicApi anthropicApi = createAnthropicApi();
		AnthropicChatOptions chatOptions = createAnthropicChatOptions();

		return AnthropicChatModel.builder()
			.anthropicApi(anthropicApi)
			.defaultOptions(chatOptions)
			.toolCallingManager(toolCallingManager)
			.observationRegistry(createObservationRegistry())
			.retryTemplate(retryTemplate)
			.build();
	}

	/**
	 * Anthropic API 클라이언트 생성
	 */
	private AnthropicApi createAnthropicApi() {
		return AnthropicApi.builder()
			.apiKey(Objects.requireNonNull(anthropicProperties.apiKey()))
			.build();
	}

	/**
	 * Anthropic 채팅 옵션 생성
	 */
	private AnthropicChatOptions createAnthropicChatOptions() {
		String model = anthropicProperties.chat().options().model();
		return AnthropicChatOptions.builder()
			.model(model)
			.maxTokens(10000)
			.build();
	}

	private ObservationRegistry createObservationRegistry() {
		return ObservationRegistry.create();
	}

	private String getModelName(String provider) {
		return switch (provider.toLowerCase()) {
			case OPEN_AI -> openAiProperties.chat().options().model();
			case ANTHROPIC -> anthropicProperties.chat().options().model();
			case GOOGLE_GEN_AI -> geminiProperties.chat().options().model();
			default -> NOT_DEFIEND;
		};
	}
}