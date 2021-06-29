package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ApiResponseConverter {

    protected <T> T convert(String body, Class <T> clazz) throws IOException {
        Reader reader = new StringReader(body);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(reader, clazz);
    }
}
