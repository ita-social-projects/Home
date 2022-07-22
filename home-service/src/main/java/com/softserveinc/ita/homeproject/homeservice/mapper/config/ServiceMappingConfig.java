package com.softserveinc.ita.homeproject.homeservice.mapper.config;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ILLEGAL_STATE_RESOLVING_TYPE_MESSAGE;

import org.modelmapper.TypeMap;
import org.springframework.core.GenericTypeResolver;


public interface ServiceMappingConfig<S, D> {
    void addMappings(TypeMap<S, D> typeMap);

    @SuppressWarnings("unchecked cast")
    default Class<S> getSourceType() {
        return (Class<S>) getResolvedClasses()[0];
    }

    @SuppressWarnings("unchecked cast")
    default Class<D> getDestinationType() {
        return (Class<D>) getResolvedClasses()[1];
    }

    private Class<?>[] getResolvedClasses() {
        Class<?>[] typeArguments =
                GenericTypeResolver.resolveTypeArguments(this.getClass(), ServiceMappingConfig.class);
        final int expectedTypeArgumentsSize = 2;
        if (typeArguments == null || typeArguments.length != expectedTypeArgumentsSize) {
            throw new IllegalStateException(ILLEGAL_STATE_RESOLVING_TYPE_MESSAGE);
        }
        return typeArguments;
    }
}
