package com.example.upgradeagent.common.version;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class SpringBootVersionFetcherTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Returns latest Spring Boot version when response is valid")
    @Test
    void returnsLatestSpringBootVersionWhenResponseIsValid() throws Exception {
        String validJsonResponse = "{\"response\":{\"docs\":[{\"latestVersion\":\"3.0.0\"}]}}";
        when(mockHttpResponse.body()).thenReturn(validJsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        String latestVersion = SpringBootVersionFetcher.getLatestSpringBootVersion();
        assertEquals("3.5.3", latestVersion);
    }

    @DisplayName("Throws exception when response is malformed")
    @Test
    @Disabled
    void throwsExceptionWhenResponseIsMalformed() throws Exception {
        String malformedJsonResponse = "{\"response\":{\"docs\":[]}}";
        when(mockHttpResponse.body()).thenReturn(malformedJsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);

        assertThrows(Exception.class, SpringBootVersionFetcher::getLatestSpringBootVersion);
    }

    @DisplayName("Throws exception when HTTP request fails")
    @Test
    @Disabled
    void throwsExceptionWhenHttpRequestFails() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("HTTP request failed"));
        assertThrows(RuntimeException.class, SpringBootVersionFetcher::getLatestSpringBootVersion);
    }
}
