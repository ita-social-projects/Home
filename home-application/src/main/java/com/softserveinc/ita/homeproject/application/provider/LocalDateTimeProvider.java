package com.softserveinc.ita.homeproject.application.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class LocalDateTimeProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> clazz, Type type, Annotation[] annotations) {
        if (clazz.equals(LocalDateTime.class)) {

            return new ParamConverter<>() {

                @SuppressWarnings("unchecked")
                @Override
                public T fromString(String value) {
                    if (value == null) {
                        return null;
                    } else {
                        LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        return (T) localDateTime;
                    }
                }

                @Override
                public String toString(T time) {
                    return LocalDateTime.parse(time.toString()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
            };
        }
        return null;
    }
}
