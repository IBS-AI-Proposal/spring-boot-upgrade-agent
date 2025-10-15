package com.example.upgradeagent.external;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestSystemMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ExternalLLM {

    @Autowired
    private Environment env;

    public String askGpt(final String prompt) {
        String apiKey = env.getProperty("project.authKey");
        String endpoint = env.getProperty("project.endpoint");
        String model = env.getProperty("project.endpoint");

        ChatCompletionsClient client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(endpoint)
                .buildClient();

        ChatCompletionsOptions chatCompletionsOptions = getChatCompletionsOptions(model, prompt);

        ChatCompletions completions = client.complete(chatCompletionsOptions);
        return completions.getChoices().get(0).getMessage().getContent();
    }

    @NotNull
    private ChatCompletionsOptions getChatCompletionsOptions(final String model, final String prompt) {
        List<ChatRequestMessage> chatMessages = Arrays.asList(
                new ChatRequestSystemMessage(""),
                new ChatRequestUserMessage(prompt)
        );

        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
        chatCompletionsOptions.setModel(model);
        return chatCompletionsOptions;
    }

}
