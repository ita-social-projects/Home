package com.softserveinc.ita.homeproject.api.tests;

import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ServerConfiguration;
import org.springframework.boot.json.JacksonJsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ApiClientUtil {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public ApiClient getClient() throws Exception {
        String applicationExternalPort = System.getProperty("home.application.external.port");
        String applicationAdminUsername = "admin@example.com";
        String applicationAdminPassword = "password";
        String applicationClientId = "client";
        String applicationClientSecret = "secret";
        ApiClient client = new ApiClient();
        client.setBearerToken(retrieveAccessToken(applicationAdminUsername, applicationAdminPassword, applicationClientId, applicationClientSecret));
        client.setServers(List.of(new ServerConfiguration("http://localhost:" + applicationExternalPort + "/api/0",
                "No description provided", new HashMap())));
        return client;
    }

    private String retrieveAccessToken(String username, String password, String clientId, String clientSecret) throws IOException, InterruptedException {
        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "password");
        data.put("password", password);
        data.put("username", username);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(data))
                .uri(URI.create("http://localhost:8080/oauth/token"))
                .setHeader("Authorization", encodeAuthorizationHeader(clientId, clientSecret))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response.body()).get("access_token").toString();
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    private String encodeAuthorizationHeader(String clientId, String clientSecret) {
        return "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }
}
