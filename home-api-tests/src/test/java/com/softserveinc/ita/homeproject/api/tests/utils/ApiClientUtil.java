package com.softserveinc.ita.homeproject.api.tests.utils;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ServerConfiguration;
import org.glassfish.jersey.logging.LoggingFeature;

public final class ApiClientUtil {

    public static ApiClient getClient() {
        String applicationExternalPort = System.getProperty("home.application.external.port");
        String applicationAdminUsername = System.getProperty("home.application.admin.username");
        String applicationAdminPassword = System.getProperty("home.application.admin.password");
        String verboseLogging = System.getProperty("verbose.tests.logging", "true");
        ApiClient client = new ApiClient();
        Logger logger = Logger.getLogger(ApiClient.class.getName());
        Level levelOfLogging = Level.WARNING;
        if (Boolean.parseBoolean(verboseLogging)) {
            levelOfLogging = Level.INFO;
        }
        client.getHttpClient()
            .register(new LoggingFeature(logger, levelOfLogging, LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));
        client.setUsername(applicationAdminUsername);
        client.setPassword(applicationAdminPassword);
        client.setServers(List.of(new ServerConfiguration("http://localhost:" + applicationExternalPort + "/api/0",
            "No description provided", new HashMap())));
        return client;
    }
}

