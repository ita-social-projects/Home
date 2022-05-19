package com.softserveinc.ita.homeproject.api.tests.security;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientForLogoutUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.LogoutContent;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.LogoutApi;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LogoutIT {

    private static LogoutApi logoutApi;
    private static CooperationApi cooperationApi;
    private static ApiClient apiClient;
    private static List<String> access_tokens;
    private final int UNAUTHORIZED = Response.Status.UNAUTHORIZED.getStatusCode();
    private final String UNAUTHORIZED_MESSAGE = "Unauthorized";
    private final String LAST_ACCESS_TOKEN = access_tokens.get(access_tokens.size() - 1);

    @BeforeAll
    static void init() {
        LogoutContent logoutContent = ApiClientForLogoutUtil.getCooperationAdminClientForLogoutAll();
        apiClient = logoutContent.getApiClient();
        access_tokens = logoutContent.getAccess_tokens();
    }

    @Test
    @SneakyThrows
    void logout() {
        logoutApi = new LogoutApi(apiClient);
        cooperationApi = new CooperationApi(apiClient);

        logoutApi.logout();

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(
                () -> cooperationApi.getCooperation(100L))
            .matches(exception -> exception.getCode() == UNAUTHORIZED)
            .withMessageContaining(UNAUTHORIZED_MESSAGE);
    }

    @Test
    @SneakyThrows
    void testLogoutAll() {
        logoutApi = new LogoutApi(apiClient.setBearerToken(LAST_ACCESS_TOKEN));

        logoutApi.logoutAll();

        for (String token : access_tokens) {
            cooperationApi = new CooperationApi(apiClient.setBearerToken(token));
            assertThatExceptionOfType(ApiException.class)
                .isThrownBy(
                    () -> cooperationApi.getCooperation(100L))
                .matches(exception -> exception.getCode() == UNAUTHORIZED)
                .withMessageContaining(UNAUTHORIZED_MESSAGE);
        }
    }
}
