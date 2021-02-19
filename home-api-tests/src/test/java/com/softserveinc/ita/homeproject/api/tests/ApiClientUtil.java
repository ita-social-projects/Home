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
        //mvn clean install -Papi-tests-infrastructure -Papi-tests -Dpostgres.user=postgres -Dpostgres.password=password -Dhome.application.admin.username=admin@example.com -Dhome.application.admin.password=password -Dhome.application.client.id=client -Dhome.application.client.secret=secret
        String applicationExternalPort = System.getProperty("home.application.external.port");
        /*String applicationAdminUsername = System.getProperty("home.application.admin.username");
        String applicationAdminPassword = System.getProperty("home.application.admin.password");
        String applicationClientId = System.getProperty("home.application.client.id");
        String applicationClientSecret = System.getProperty("home.application.client.secret");*/
        //String applicationExternalPort = "8080";
        String applicationAdminUsername = "admin@example.com";
        String applicationAdminPassword = "password";
        String applicationClientId = "client";
        String applicationClientSecret = "secret";
        ApiClient client = new ApiClient();
        client.setAccessToken(retrieveAccessToken(applicationAdminUsername, applicationAdminPassword, applicationClientId, applicationClientSecret, applicationExternalPort));
        client.setServers(List.of(new ServerConfiguration("http://localhost:" + applicationExternalPort + "/api/0",
                "No description provided", new HashMap())));
        return client;
    }

    private String retrieveAccessToken(String username, String password, String clientId, String clientSecret, String port) throws IOException, InterruptedException {
        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "password");
        data.put("password", password);
        data.put("username", username);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(data))
                .uri(URI.create("http://localhost:" + port + "/oauth/token"))
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
