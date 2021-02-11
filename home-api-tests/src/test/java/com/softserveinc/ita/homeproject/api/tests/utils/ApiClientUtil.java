package com.softserveinc.ita.homeproject.api.tests.utils;

import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ServerConfiguration;

import java.util.HashMap;
import java.util.List;

public final class ApiClientUtil {

    public static ApiClient getClient() {
        String applicationExternalPort = System.getProperty("home.application.external.port");
        String applicationAdminUsername = System.getProperty("home.application.admin.username");
        String applicationAdminPassword = System.getProperty("home.application.admin.password");
        ApiClient client = new ApiClient();
        client.setUsername(applicationAdminUsername);
        client.setPassword(applicationAdminPassword);
        client.setServers(List.of(new ServerConfiguration("http://localhost:"+applicationExternalPort+"/api/0",
                "No description provided", new HashMap())));
        return client;
    }
}

