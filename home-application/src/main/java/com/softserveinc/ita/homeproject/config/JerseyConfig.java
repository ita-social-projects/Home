package com.softserveinc.ita.homeproject.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api/0")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // register endpoints
        packages("com.softserveinc.ita.homeproject.api");
        // register jackson for json
        register(JacksonJsonProvider.class);
    }
}
