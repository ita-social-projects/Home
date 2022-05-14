package com.softserveinc.ita.homeproject.api.tests.utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.LogoutContent;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.model.ReadUser;

public class ApiClientForLogoutUtil {

    private static final int COUNT_OF_ACCESS_TOKENS = 3;

    public static LogoutContent getCooperationAdminClientForLogoutAll() {
        if (logoutContent == null) {
            logoutContent = generateCoopAdminClientForLogoutAll();
        }
        return logoutContent;
    }    private static LogoutContent logoutContent = getCooperationAdminClientForLogoutAll();

    private static LogoutContent generateCoopAdminClientForLogoutAll() {
        ReadUser cooperationAdmin = ApiClientUtil.createCooperationAdmin();
        ApiClient client = new ApiClient();
        ApiClientUtil.setLoggingFeature(client);
        ArrayList<String> access_tokens = getAccessTokensSequence(cooperationAdmin);
        client.setBearerToken(access_tokens.get(0));
        ApiClientUtil.setServers(client);
        return LogoutContent.builder().apiClient(client).access_tokens(access_tokens).build();
    }

    private static ArrayList<String> getAccessTokensSequence(ReadUser cooperationAdmin) {
        ArrayList<String> access_tokens = new ArrayList<>();
        for (int i = 0; i <= COUNT_OF_ACCESS_TOKENS; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                access_tokens.add(ApiClientUtil.getAuthentication(cooperationAdmin));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return access_tokens;
    }


}
