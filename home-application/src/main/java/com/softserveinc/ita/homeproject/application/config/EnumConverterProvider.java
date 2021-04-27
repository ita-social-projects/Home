package com.softserveinc.ita.homeproject.application.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

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

                throw new NotFoundException("The parameter type must be one of the enumeration constants");
            }

            @Override
            public String toString(T value) {
                return value.toString();
            }
        };
    }
}
