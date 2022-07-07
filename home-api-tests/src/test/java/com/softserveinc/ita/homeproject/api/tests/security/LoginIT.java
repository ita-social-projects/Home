package com.softserveinc.ita.homeproject.api.tests.security;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.getCooperationAdminClient;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.getUserGuestClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.ServerConfiguration;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.client.model.CreateUser;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import com.softserveinc.ita.homeproject.homeoauthserver.api.AuthenticationApi;
import com.softserveinc.ita.homeproject.homeoauthserver.model.UserCredentials;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoginIT {

    private static final String APPLICATION_EXTERNAL_PORT = System.getProperty("home.application.external.port");

    private static final String OAUTH_APPLICATION_EXTERNAL_PORT = System.getProperty("home.oauth.external.port");

    private static final String VERBOSE_LOGGING = System.getProperty("verbose.tests.logging", "true");

    private static final String USER_EMAIL = RandomStringUtils.randomAlphabetic(10).concat("@gmail.com");

    private static final String USER_PASSWORD = "password";

    private static final CooperationApi cooperationApi = new CooperationApi(getCooperationAdminClient());

    private static final UserApi userApi = new UserApi(getUserGuestClient());

    private static AuthenticationApi authenticationApi;


    @Test
    @SneakyThrows
    void loginTest() {
        createCooperationAdmin();
        UserCredentials userCredentials = new UserCredentials()
            .email(USER_EMAIL)
            .password(USER_PASSWORD);
        authenticationApi = new AuthenticationApi(getAuthenticationApi());


        Assertions.assertDoesNotThrow(() -> authenticationApi.authenticateUser(userCredentials));

    }

    @SneakyThrows
    static ReadUser createCooperationAdmin() {
        CreateCooperation createCoop = createBaseCooperation();
        CreateUser expectedUser = createBaseUser();
        return ApiClientUtil.createCooperationAdmin(cooperationApi, createCoop, userApi, expectedUser);
    }

    private static com.softserveinc.ita.homeproject.homeoauthserver.ApiClient getAuthenticationApi() {
        com.softserveinc.ita.homeproject.homeoauthserver.ApiClient oauthClient =
            new com.softserveinc.ita.homeproject.homeoauthserver.ApiClient();
        AuthenticationApi authenticationApi = new AuthenticationApi();
        setOauthLoggingFeature(oauthClient);
        authenticationApi.setApiClient(oauthClient);
        setServers(oauthClient);

        return oauthClient;
    }


    static <T> void setServers(T client) {
        if (client instanceof ApiClient) {
            ((ApiClient) client).setServers(List.of(new ServerConfiguration("http://localhost:" +
                APPLICATION_EXTERNAL_PORT + "/api/0", "No description provided", new HashMap())));
        } else if (client instanceof com.softserveinc.ita.homeproject.homeoauthserver.ApiClient) {
            ((com.softserveinc.ita.homeproject.homeoauthserver.ApiClient) client).setServers(List.of
                (new com.softserveinc.ita.homeproject.homeoauthserver.ServerConfiguration("http://localhost:" +
                    OAUTH_APPLICATION_EXTERNAL_PORT + "/api/0/oauth2", "No description provided", new HashMap())));
        }
    }

    private static void setOauthLoggingFeature(com.softserveinc.ita.homeproject.homeoauthserver.ApiClient client) {
        if (Boolean.parseBoolean(VERBOSE_LOGGING)) {
            Logger logger =
                Logger.getLogger(com.softserveinc.ita.homeproject.homeoauthserver.ApiClient.class.getName());
            client.getHttpClient()
                .register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));
        }
    }

    private static CreateUser createBaseUser() {

        return new CreateUser()
            .firstName("firstName")
            .middleName("middleName")
            .lastName("lastName")
            .password(USER_PASSWORD)
            .email(USER_EMAIL)
            .contacts(createContactList());
    }

    private static List<CreateContact> createContactList() {
        List<CreateContact> createContactList = new ArrayList<>();
        createContactList.add(new CreateEmailContact()
            .email("primaryemail@example.com")
            .type(ContactType.EMAIL)
            .main(true));

        createContactList.add(new CreateEmailContact()
            .email("secondaryemail@example.com")
            .type(ContactType.EMAIL)
            .main(false));

        createContactList.add(new CreateEmailContact()
            .email("myemail@example.com")
            .type(ContactType.EMAIL)
            .main(false));

        createContactList.add(new CreateEmailContact()
            .email("youremail@example.com")
            .type(ContactType.EMAIL)
            .main(false));

        createContactList.add(new CreatePhoneContact()
            .phone("+380509999999")
            .type(ContactType.PHONE)
            .main(true));

        createContactList.add(new CreatePhoneContact()
            .phone("+380679999999")
            .type(ContactType.PHONE)
            .main(false));

        createContactList.add(new CreatePhoneContact()
            .phone("+380639999999")
            .type(ContactType.PHONE)
            .main(false));

        return createContactList;
    }

    private static CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
            .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminEmail(USER_EMAIL)
            .address(createAddress());
    }


    private static Address createAddress() {
        return new Address().city("Dnepr")
            .district("District")
            .houseBlock("block")
            .houseNumber("number")
            .region("Dnipro")
            .street("street")
            .zipCode("zipCode");
    }
}
