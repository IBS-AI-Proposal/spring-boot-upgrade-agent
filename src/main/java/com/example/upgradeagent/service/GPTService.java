package com.example.upgradeagent.service;

import com.example.upgradeagent.common.PromptBuilder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class GPTService {

    @Autowired
    private Environment env;


    @Value("${spring.ai.openai.base-url}")
    private String openAiBaseUrl;

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;

    @Value("${spring.ai.openai.chat.model}")
    private String openAiModel;

    @Value("${spring.ai.ollama.chat.model}")
    private String ollamaModel;

    @Value("${spring.ai.ollama.base-url}")
    private String ollamaBaseUrl;


    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Autowired
    private OpenAiChatModel baseChatModel;

    @Autowired
    private OpenAiApi baseOpenAiApi;


    public String getLatestOpenRewriteRecipe(final String version) {
        String prompt = PromptBuilder.latestOpenRewritePrompt(version);
        ChatClient client = getDefaultChatClient();
        return client.prompt(prompt).call().content();
    }

    private ChatClient getDefaultChatClient() {
        OpenAiApi gpt4Api = baseOpenAiApi.mutate()
                .baseUrl(openAiBaseUrl)
                .apiKey(openAiKey)
                .build();

        OpenAiApi ollamaApi = baseOpenAiApi.mutate()
                .baseUrl(ollamaBaseUrl)
                .build();

        OpenAiChatModel gpt4Model = baseChatModel.mutate()
                .openAiApi(gpt4Api)
                .defaultOptions(OpenAiChatOptions.builder().model(openAiModel).temperature(0.7).build())
                .build();

        OpenAiChatModel ollamaChatModel = baseChatModel.mutate()
                .openAiApi(ollamaApi)
                .defaultOptions(OpenAiChatOptions.builder().model(ollamaModel).temperature(0.7).build())
                .build();
        String provider = env.getProperty("spring.ai.default-provider", "ollama");

        ChatModel model =  switch (provider.toLowerCase()) {
            case "openai" -> gpt4Model;
            case "ollama" -> ollamaChatModel;
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        };

        return ChatClient.builder(model).build();
    }
}

