package com.example.upgradeagent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LLMClient {
    private static final String API_KEY = "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";
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    public static final String MODEL = "model";
    public static final String GPT_4 = "gpt-4";
    public static final String MESSAGES = "messages";
    public static final String TEMPERATURE = "temperature";
    public static final String MAX_TOKENS = "max_tokens";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    public static String queryLLM(String prompt) throws IOException {
        Map<String, Object> body = Map.of(
                MODEL, GPT_4,
                MESSAGES, List.of(Map.of("role", "user", "content", prompt)),
                TEMPERATURE, 0.7,
                MAX_TOKENS, 2000
        );
        String json = mapper.writeValueAsString(body);

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .addHeader(AUTHORIZATION, "Bearer " + API_KEY)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .post(RequestBody.create(json, MediaType.get(APPLICATION_JSON)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())  {
                throw new IOException("Unexpected code " + response);
            }
            JsonNode jsonResponse = mapper.readTree(response.body().string());
            return jsonResponse.path("choices").get(0).path("message").path("content").asText();
        }
    }
}
