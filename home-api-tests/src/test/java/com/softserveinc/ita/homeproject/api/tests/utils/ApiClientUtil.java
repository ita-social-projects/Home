package com.softserveinc.ita.homeproject.api.tests.utils;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
import com.softserveinc.ita.homeproject.client.api.ApartmentApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.HouseApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ApiError;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateApartment;
import com.softserveinc.ita.homeproject.client.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreateInvitation;
import com.softserveinc.ita.homeproject.client.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.client.model.CreateUser;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.logging.LoggingFeature;

public final class ApiClientUtil {

    private static ApiClient unauthorizedGuestClient;

    private static ApiClient authorizedCoopAdminClient;

    private static ApiClient authorizedOwnerAdminClient;

    public static final int BAD_REQUEST = Response.Status.BAD_REQUEST.getStatusCode();

    public static final int NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();

    private static final String APPLICATION_EXTERNAL_PORT = System.getProperty("home.application.external.port");

    private static final String VERBOSE_LOGGING = System.getProperty("verbose.tests.logging", "true");

    private static final String USER_PASSWORD = "password";

    private static final UserApi userApi = new UserApi(getUserGuestClient());

    private static final CooperationApi cooperationApi = new CooperationApi(getUserGuestClient());

    private static final HouseApi houseApi = new HouseApi(getCooperationAdminClient());

    private static final ApartmentApi apartmentApi = new ApartmentApi(getCooperationAdminClient());

    public static ApiClient getCooperationAdminClient() {
        if (authorizedCoopAdminClient == null) {
            authorizedCoopAdminClient = generateCoopAdminClient();
        }
        return authorizedCoopAdminClient;
    }

    public static ApiClient getOwnerClient() {
        if (authorizedOwnerAdminClient == null) {
            authorizedOwnerAdminClient = generateOwnerUserClient();
        }
        return authorizedOwnerAdminClient;
    }

    public static ApiClient getUserGuestClient() {
        if (unauthorizedGuestClient == null) {
            unauthorizedGuestClient = generateUnauthorizedClient();
        }
        return unauthorizedGuestClient;
    }

    private static ApiClient generateCoopAdminClient() {
        ReadUser cooperationAdmin = createCooperationAdmin();
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        client.setUsername(cooperationAdmin.getEmail());
        client.setPassword(USER_PASSWORD);
        setServers(client);
        return client;
    }

    private static ApiClient generateOwnerUserClient() {
        ReadUser ownerUser = createOwnerUser();
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        client.setUsername(ownerUser.getEmail());
        client.setPassword(USER_PASSWORD);
        setServers(client);
        return client;
    }

    private static ApiClient generateUnauthorizedClient() {
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        setServers(client);
        return client;
    }


    @SneakyThrows
    private static ReadUser createCooperationAdmin() {
        CreateCooperation createCoop = createBaseCooperation();
        cooperationApi.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse, createCoop.getAdminEmail())));
        expectedUser.setEmail(createCoop.getAdminEmail());
        return userApi.createUser(expectedUser);
    }

    private static CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomNumeric(8))
                .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
                .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
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

    private static CreateUser createBaseUser() {

        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password(USER_PASSWORD)
                .email("test.receive.apartment@gmail.com")
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

    @SneakyThrows
    static ReadUser createOwnerUser() {
        CreateApartment createApartment = createApartment();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createBaseCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        apartmentApi.createApartment(createdHouse.getId(), createApartment);

        TimeUnit.MILLISECONDS.sleep(5000);
        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);
        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse,
                Objects.requireNonNull(createApartment.getInvitations()).get(0).getEmail())));

        expectedUser.setEmail(Objects.requireNonNull(createApartment.getInvitations()).get(0).getEmail());

        return userApi.createUser(expectedUser);
    }

    private static CreateHouse createHouse() {
        return new CreateHouse()
                .adjoiningArea(500)
                .houseArea(BigDecimal.valueOf(500.0))
                .quantityFlat(50)
                .address(createAddress());
    }

    private static CreateApartment createApartment() {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15")
                .invitations(createApartmentInvitation());
    }

    private static List<CreateInvitation> createApartmentInvitation() {
        List<CreateInvitation> createInvitations = new ArrayList<>();
        createInvitations.add(new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.3))
                .email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .type(InvitationType.APARTMENT));

        return createInvitations;
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
}
