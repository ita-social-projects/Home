package com.softserveinc.ita.homeproject.api.tests.utils;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ServerConfiguration;
import org.glassfish.jersey.logging.LoggingFeature;

public final class ApiClientUtil {
    private static final String APPLICATION_EXTERNAL_PORT = System.getProperty("home.application.external.port");
    private static final String APPLICATION_ADMIN_USER_NAME = System.getProperty("home.application.admin.username");
    private static final String APPLICATION_ADMIN_PASSWORD = System.getProperty("home.application.admin.password");
    private static final String VERBOSE_LOGGING = System.getProperty("verbose.tests.logging", "true");
    private static final ApiClient CLIENT = new ApiClient();

    public static ApiClient getClient() {
        setLoggingFeature();
        CLIENT.setUsername(APPLICATION_ADMIN_USER_NAME);
        CLIENT.setPassword(APPLICATION_ADMIN_PASSWORD);
        CLIENT.setServers(List.of(new ServerConfiguration("http://localhost:" +
            APPLICATION_EXTERNAL_PORT + "/api/0", "No description provided", new HashMap())));
        return CLIENT;
    }

    public static ApiClient getUnauthorizedClient() {
        setLoggingFeature();
        CLIENT.setServers(List.of(new ServerConfiguration("http://localhost:" +
            APPLICATION_EXTERNAL_PORT + "/api/0", "No description provided", new HashMap())));
        return CLIENT;
    }

    private static void setLoggingFeature() {
        if (Boolean.parseBoolean(VERBOSE_LOGGING)) {
            Logger logger = Logger.getLogger(ApiClient.class.getName());
            CLIENT.getHttpClient()
               .register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));
        }
    }
}
