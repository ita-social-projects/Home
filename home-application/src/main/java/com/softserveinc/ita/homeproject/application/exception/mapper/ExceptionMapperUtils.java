package com.softserveinc.ita.homeproject.application.exception.mapper;

import java.util.Arrays;
import java.util.Optional;

import cz.jirutka.rsql.parser.RSQLParserException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;

public final class ExceptionMapperUtils {

    private ExceptionMapperUtils() {

    }

    public static String getBadRequestExceptionMessageParser(RSQLParserException exception) {
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

    public static String getInvalidDataApiExceptionParser(InvalidDataAccessApiUsageException exception) {
        String message = null;
        String exceptionMessage = exception.getMessage();
        if (!StringUtils.isBlank(exceptionMessage)) {
            String[] str = exceptionMessage.split("com", 2);
            Optional<String> result = Arrays.stream(str).findFirst();
            if (result.isPresent()) {
                if (result.get().contains("[")) {
                    message = getProperty(result.get()).toString().trim();
                } else {
                    message = result.get().trim();
                }
            }
        }
        return message;
    }

    private static StringBuilder getProperty(String result) {
        StringBuilder sb = new StringBuilder(result);
        sb.delete(0, sb.indexOf("[") + 1);
        sb.delete(sb.indexOf("]"), sb.length());
        sb.insert(0, "Unknown property:").append(" from entity");
        return sb;
    }
}

