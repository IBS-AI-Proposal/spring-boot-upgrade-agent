package com.example.upgradeagent.common.version;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpringBootVersionFetcher {

    private static SpringBootVersionFetcher instance;

    private SpringBootVersionFetcher() {
        // private constructor to prevent instantiation
    }

    public static SpringBootVersionFetcher getInstance() {
        if (instance == null) {
            instance = new SpringBootVersionFetcher();
        }
        return instance;
    }

    public static String getLatestSpringBootVersion() throws Exception {
        String url = "https://search.maven.org/solrsearch/select?q=g:org.springframework.boot%20AND%20a:spring-boot&rows=1&wt=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        return root.path("response")
                .path("docs")
                .get(0)
                .path("latestVersion")
                .asText();
    }
}
