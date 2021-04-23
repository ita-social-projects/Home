package com.softserveinc.ita.homeproject.application.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.modelmapper.spi.ErrorMessage;

@SuppressWarnings("unchecked")
@Provider
public class EnumConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (!rawType.isEnum()) {
            return null;
        }

        final Class<Enum<?>> type = (Class<Enum<?>>) rawType;
        final Enum<?>[] constants = type.getEnumConstants();

        return new ParamConverter<T>() {
            @Override
            public T fromString(String value) {
                if (value == null || value.isEmpty()) {
                    return null;
                }

                for (Enum<?> constant : constants) {
                    if (constant.toString().equals(value)) {
                        return (T) constant;
                    }
                }

                throw new BadRequestException(
                    getErrorResponse("Parameter type must be one of enum constants"));
            }

            @Override
            public String toString(T value) {
                return value.toString();
            }

            protected Response getErrorResponse(String message) {
                return Response
                    .status(400)
                    .entity(new ErrorMessage(message))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
            }
        };
    }
}
