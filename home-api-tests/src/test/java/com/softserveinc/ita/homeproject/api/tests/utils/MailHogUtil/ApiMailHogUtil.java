package com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil.Dto.ResponseDto;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public final class ApiMailHogUtil {

    private static final String MAIL_HOG_HOST = System.getProperty("MAIL_HOST");
    private static final String MAIL_HOG_HOST2 = System.getProperty("send.email.test.host");

    private static final Integer MAIL_HOG_PORT = 8025;

    private static String getApiAddress(){
        return "http://"+MAIL_HOG_HOST+":"+MAIL_HOG_PORT+"/api/v2/";
    }

    // receive messages
    public static ResponseDto getMessages() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getApiAddress()+"messages"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        System.out.println(getApiAddress());
        HttpResponse <String> response = HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        Reader reader = new StringReader(response.body());
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(reader,ResponseDto.class);
    }






}

