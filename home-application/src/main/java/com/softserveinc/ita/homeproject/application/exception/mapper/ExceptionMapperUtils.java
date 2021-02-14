package com.softserveinc.ita.homeproject.application.exception.mapper;

import cz.jirutka.rsql.parser.RSQLParserException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Arrays;
import java.util.Optional;

public final class ExceptionMapperUtils {

    public static String RSQLExceptionMessageParser(RSQLParserException exception) {
        String message = null;
        if (!exception.getMessage().contains("<EOF>")) {
            String[] str = exception.getMessage().split(":", 2);
            Optional<String> result = Arrays.stream(str).skip(1).findFirst();
            if (result.isPresent()) {
                message = result.get().trim();
            }
        } else {
            message = "The query argument for search is empty";
        }
        return message;
    }

    public static String invalidDataApiExeptionParser(InvalidDataAccessApiUsageException exception) {
        String message = null;
        String[] str = exception.getMessage().split("com", 2);
        Optional<String> result = Arrays.stream(str).findFirst();
        if (result.isPresent()) {
            message = result.get().trim();
        }
        return message;
    }
}
