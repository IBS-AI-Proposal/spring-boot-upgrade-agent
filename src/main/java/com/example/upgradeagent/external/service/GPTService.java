package com.example.upgradeagent.external.service;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestSystemMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class GPTService {
    public static String askGpt(String prompt, String apiKey) {

        String endpoint = "https://models.github.ai/inference";
        String model = "openai/gpt-4.1";

        ChatCompletionsClient client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(endpoint)
                .buildClient();

        ChatCompletionsOptions chatCompletionsOptions = getChatCompletionsOptions(model, prompt);

        ChatCompletions completions = client.complete(chatCompletionsOptions);
        return completions.getChoices().get(0).getMessage().getContent();
    }

    @NotNull
    private static ChatCompletionsOptions getChatCompletionsOptions(String model, String prompt) {
        List<ChatRequestMessage> chatMessages = Arrays.asList(
                new ChatRequestSystemMessage(""),
                new ChatRequestUserMessage(prompt)
        );

        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
        chatCompletionsOptions.setModel(model);
        return chatCompletionsOptions;
    }

    public static String getLatestSpringBootVersion(String apiKey) {
        String prompt = "Provide only the latest stable version of Spring Boot as a response, without any additional text.\"";
        return askGpt(prompt, apiKey).trim();
    }

    public static String getSpringBootVersionUpgradeSuggestions(String apiKey) {
        String prompt = "I want to upgrade my Spring Boot project in this workspace, located at `/Users/Vishnu/REPOS/AIDEMO/ecommerce-auditlog`, to the latest stable version (or a specific version like 3.2.x). Please do the following:\\n\\n1. Identify the current Spring Boot version from `pom.xml` or `build.gradle` in the given path.\\n\\n2. Update it to the latest compatible version, and modify other dependencies/plugins accordingly.\\n\\n3. Check for:\\n   - Deprecated or removed libraries\\n   - Spring Security changes (e.g., `WebSecurityConfigurerAdapter` removal)\\n   - Jakarta namespace migrations (javax → jakarta)\\n   - Any changes required in `application.properties` or `application.yml`\\n\\n4. Scan `src/main/java/` for deprecated APIs or breaking changes, and suggest or make replacements.\\n\\n5. Suggest or apply OpenRewrite recipes or other tools to help automate this process.\\n\\n6. Create a summary of changes and any manual steps left to complete the upgrade.\\n\\nThis is a Maven-based Spring Boot project. Assume standard structure and plugins. Applies to the workspace project path: `/Users/Vishnu/REPOS/AIDEMO/ecommerce-auditlog`.\"";
        return askGpt(prompt, apiKey).trim();
    }

    public static String getLatestOpenRewriteRecipe(String apiKey, String version) {
        String prompt = "Give me the correct, existing OpenRewrite recipe ID for upgrading to Spring Boot %s — only if it exists in the official rewrite-spring module, no backticks, no formatting.\"";
        prompt = String.format(prompt, version);
        return askGpt(prompt, apiKey).trim();
    }
}

