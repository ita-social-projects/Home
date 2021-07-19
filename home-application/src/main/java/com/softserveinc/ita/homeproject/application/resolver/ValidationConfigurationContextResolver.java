package com.softserveinc.ita.homeproject.application.resolver;

import javax.ws.rs.ext.ContextResolver;

import com.softserveinc.ita.homeproject.application.provider.JerseyParameterNameProvider;
import org.glassfish.jersey.server.validation.ValidationConfig;

public class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {

    @Override
    public ValidationConfig getContext(final Class<?> type) {
        final var config = new ValidationConfig();
        config.parameterNameProvider(new JerseyParameterNameProvider());
        return config;
    }
}
