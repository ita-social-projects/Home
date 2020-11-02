package com.softserveinc.ita.homeproject.config;

import com.softserveinc.ita.homeproject.api.NewsApi;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(NewsApi.class);
    }
}