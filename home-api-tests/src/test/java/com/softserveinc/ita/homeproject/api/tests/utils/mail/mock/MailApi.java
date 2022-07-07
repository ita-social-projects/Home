package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Mailhog API client.
 */
class MailApi {

    private static final String MAIL_API_PORT = System.getProperty("mailhog.external.port");

    public MailHogApiResponse getMessages() throws IOException, InterruptedException {
        String json = getJson(getMessagesUrl());
        return convert(json, MailHogApiResponse.class);
    }

    private String getMessagesUrl() {
        return getApiUri() + getMessagesEndpoint();
    }

    private String getApiUri() {
        return "http://localhost:" + MAIL_API_PORT + "/api/v2/";
    }

    private String getMessagesEndpoint() {
        return "messages";
    }

    private String getJson(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private <T> T convert(String body, Class <T> clazz) throws IOException {
        Reader reader = new StringReader(body);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reader, clazz);
    }
}
