package com.softserveinc.ita.homeproject.application.provider;

import java.text.SimpleDateFormat;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * Basic implementation of JAX-RS abstraction needed for binding
 * JSON ("application/json") content to and from Java Objects ("POJO"s).
 *
 * @author Mykyta Morar
 */
@Provider
@Produces({MediaType.APPLICATION_JSON})
public class JacksonJsonProvider extends JacksonJaxbJsonProvider {

    /**
     * Default constructor, provider, is automatically configured
     * for usage with JAX-RS implementation.
     */
    public JacksonJsonProvider() {
        ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.WRAP_EXCEPTIONS)
            .disable(SerializationFeature.WRAP_EXCEPTIONS)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(new JavaTimeModule())
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        setMapper(objectMapper);
    }
}
