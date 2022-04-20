package com.softserveinc.ita.homeproject.application.config.resolver;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.provider.JerseyParameterNameProvider;
import org.glassfish.jersey.server.validation.ValidationConfig;

@Provider
public class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {

    @Override
    public ValidationConfig getContext(final Class<?> type) {
        final ValidationConfig config = new ValidationConfig();
        config.parameterNameProvider(new JerseyParameterNameProvider());
        return config;
    }
}
