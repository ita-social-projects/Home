package com.softserveinc.ita.homeproject.api.tests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ServerConfiguration;
import com.softserveinc.ita.homeproject.client.model.ApiError;
import lombok.SneakyThrows;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ApiClientUtil {

    public static final int BAD_REQUEST = Response.Status.BAD_REQUEST.getStatusCode();
    public static final int NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();
    private static final String APPLICATION_EXTERNAL_PORT = System.getProperty("home.application.external.port");
    private static final String APPLICATION_ADMIN_USER_NAME = System.getProperty("home.application.admin.username");
    private static final String APPLICATION_ADMIN_PASSWORD = System.getProperty("home.application.admin.password");
    private static final String APPLICATION_COOPERATION_ADMIN_USER_NAME = System.getProperty("home.application.cooperationadmin.username");
    private static final String APPLICATION_COOPERATION_ADMIN_PASSWORD = System.getProperty("home.application.cooperationadmin.password");
    private static final String APPLICATION_OWNER_USER_NAME = System.getProperty("home.application.owner.username");
    private static final String APPLICATION_OWNER_PASSWORD = System.getProperty("home.application.owner.password");
    private static final String VERBOSE_LOGGING = System.getProperty("verbose.tests.logging", "true");

    public static ApiClient getAdminClient() {
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        client.setUsername(APPLICATION_ADMIN_USER_NAME);
        client.setPassword(APPLICATION_ADMIN_PASSWORD);
        setServers(client);
        return client;
    }

    public static ApiClient getCooperationAdminClient() {
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        client.setUsername(APPLICATION_COOPERATION_ADMIN_USER_NAME);
        client.setPassword(APPLICATION_COOPERATION_ADMIN_PASSWORD);
        setServers(client);
        return client;
    }

    public static ApiClient getOwnerClient() {
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        client.setUsername(APPLICATION_OWNER_USER_NAME);
        client.setPassword(APPLICATION_OWNER_PASSWORD);
        setServers(client);
        return client;
    }

    public static ApiClient getUnauthorizedUserClient() {
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        setServers(client);
        return client;
    }

    private static void setLoggingFeature(ApiClient client) {
        if (Boolean.parseBoolean(VERBOSE_LOGGING)) {
            Logger logger = Logger.getLogger(ApiClient.class.getName());
            client.getHttpClient()
                .register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));
        }
    }

    private static void setServers(ApiClient client) {
        client.setServers(List.of(new ServerConfiguration("http://localhost:" +
            APPLICATION_EXTERNAL_PORT + "/api/0", "No description provided", new HashMap())));
    }

    @SneakyThrows
    public static String getErrorMessage(ApiException apiException) {
        return new ObjectMapper().readValue(apiException.getMessage(), ApiError.class).getErrorMessage();
    }
}
