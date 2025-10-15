package com.example.upgradeagent.common.version;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class SpringBootVersionFetcher {

    public String getLatestSpringBootVersion() throws Exception {
        String url = "https://search.maven.org/solrsearch/select?q=g:org.springframework.boot%20AND%20a:spring-boot&rows=1&wt=json";

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        return root.path("response")
                .path("docs")
                .get(0)
                .path("latestVersion")
                .asText();
    }
}
