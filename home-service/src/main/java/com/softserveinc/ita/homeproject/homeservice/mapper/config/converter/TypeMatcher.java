package com.softserveinc.ita.homeproject.homeservice.mapper.config.converter;

public interface TypeMatcher<S,D> {
    boolean match(Class<? extends S> sourceType, Class<? extends D> destinationType);
}
