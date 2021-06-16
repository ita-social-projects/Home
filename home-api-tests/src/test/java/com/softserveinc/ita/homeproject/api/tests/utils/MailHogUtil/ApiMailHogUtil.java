package com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil.Dto.ResponseDto;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class ApiMailHogUtil {

    private static final String MAIL_HOG_API_PORT = System.getProperty("mailhog.external.port");

    private static String getApiURI(){
        return "http://"+"localhost"+":"+MAIL_HOG_API_PORT+"/api/v2/";
    }

    public static ResponseDto getMessages() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getApiURI()+"messages"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse <String> response = HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return convert(response.body());
    }

    private static ResponseDto convert(String body) throws IOException {
        Reader reader = new StringReader(body);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reader,ResponseDto.class);
    }

    public static String getLastMessageEmailTo(ResponseDto responseDto){
        return String.valueOf(responseDto.getItems().get(0).getContent().getHeaders().getTo());
    }

    public static String getLastMessageSubject(ResponseDto responseDto){
        return String.valueOf(responseDto.getItems().get(0).getContent().getHeaders().getSubject());
    }

}

