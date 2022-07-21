package com.softserveinc.ita.homeproject.application.provider;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.BAD_REQUEST_HOME_WITH_PARAMETER_TYPE_MESSAGE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;

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

                throw new BadRequestHomeException(String.format(BAD_REQUEST_HOME_WITH_PARAMETER_TYPE_MESSAGE,
                    Arrays.toString(constants)));
            }

            @Override
            public String toString(T value) {
                return value.toString();
            }
        };
    }
}
