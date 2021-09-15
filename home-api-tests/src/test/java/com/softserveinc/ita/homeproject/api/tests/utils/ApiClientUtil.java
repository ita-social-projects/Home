package com.softserveinc.ita.homeproject.api.tests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ServerConfiguration;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.client.model.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    private static final UserApi USER_API = new UserApi(getAdminClient());
    private static final CooperationApi COOPERATION_API = new CooperationApi(getAdminClient());
    private Assertions AssertEquals;

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
//        InnerUserDto userCooperationAdmin = createTestUserViaInvitation();
//        client.setUsername(userCooperationAdmin.getEmail());
        client.setUsername(APPLICATION_COOPERATION_ADMIN_USER_NAME);
//        client.setPassword(userCooperationAdmin.getPassword());
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

    @SneakyThrows
    static ReadUser createCooperationAdminUser() {
        CreateUser createUser = new CreateUser()
                .firstName("User_COOPERATION_ADMIN")
                .lastName("User_COOPERATION_ADMIN")
                .password("password")
                .email("cooperation_admin@example.com");

        return USER_API.createUser(createUser);
    }

    @Data
    private static class InnerUserDto {
        private final String email;
        private final String password;
    }

    @SneakyThrows
    @Test
    public InnerUserDto createTestUserViaInvitation() {
        CreateCooperation createCoop = createBaseCooperation();
        COOPERATION_API.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse, createCoop.getAdminEmail())));
        expectedUser.setEmail(createCoop.getAdminEmail());

        assertEquals(1, 1);
        return new InnerUserDto(USER_API.createUser(expectedUser).getEmail(), expectedUser.getPassword());
    }

    private static CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(RandomStringUtils.randomAlphabetic(12).concat("@gmail.com"))
                .address(createAddress())
                .contacts(createContactList());
    }

    private static CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("test.receive.apartment@gmail.com");
    }

    private static String getToken(String str) {
        Pattern pattern = Pattern.compile("(?<=:) .* (?=<)");
        Matcher matcher = pattern.matcher(str);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }

        return result.trim();
    }

    private static String getDecodedMessageByEmail(MailHogApiResponse response, String email) {
        String message = "";
        for (int i = 0; i < response.getItems().size(); i++) {
            if (response.getItems().get(i).getContent().getHeaders().getTo().contains(email)) {
                message = response.getItems().get(i).getMime().getParts().get(0).getMime().getParts().get(0).getBody();
                break;
            }
        }
        return new String(Base64.getMimeDecoder().decode(message), StandardCharsets.UTF_8);
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

    private static List<CreateContact> createContactList() {
        List<CreateContact> createContactList = new ArrayList<>();
        createContactList.add(new CreateEmailContact()
                .email("primaryemail@example.com")
                .type(ContactType.EMAIL)
                .main(true));

        createContactList.add(new CreatePhoneContact()
                .phone("+380509999999")
                .type(ContactType.PHONE)
                .main(true));

        return createContactList;
    }
}
