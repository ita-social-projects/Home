package com.softserveinc.ita.homeproject.application.mapper.config;

import org.modelmapper.TypeMap;
import org.springframework.core.GenericTypeResolver;

public interface HomeMappingConfig<S, D> {
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
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(this.getClass(), HomeMappingConfig.class);
        final var expectedTypeArgumentsSize = 2;
        if (typeArguments == null || typeArguments.length != expectedTypeArgumentsSize) {
            throw new IllegalStateException("Something went wrong with resolving types. Try to provide it explicitly.");
        }
        return typeArguments;
    }
}
