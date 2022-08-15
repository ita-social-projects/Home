package com.softserveinc.ita.homeproject.api.tests.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.MailUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.ResponseEmailItem;
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
import com.softserveinc.ita.homeproject.client.model.CreateCooperationAdminData;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreateInvitation;
import com.softserveinc.ita.homeproject.client.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.client.model.CreateUser;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import com.softserveinc.ita.homeproject.homeoauthserver.api.AuthenticationApi;
import com.softserveinc.ita.homeproject.homeoauthserver.model.CreateToken;
import com.softserveinc.ita.homeproject.homeoauthserver.model.UserCredentials;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.logging.LoggingFeature;

public final class ApiClientUtil {

    public static final int BAD_REQUEST = Response.Status.BAD_REQUEST.getStatusCode();

    public static final int NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();

    public static final int NOT_ACCEPTABLE = Response.Status.NOT_ACCEPTABLE.getStatusCode();

    private final static int NUMBER_OF_APARTMENT_INVITATIONS = 1;

    private final static Long APARTMENT_ID = 100L;

    private static final String APPLICATION_EXTERNAL_PORT = System.getProperty("home.application.external.port");

    private static final String OAUTH_APPLICATION_EXTERNAL_PORT = System.getProperty("home.oauth.external.port");

    private static final String VERBOSE_LOGGING = System.getProperty("verbose.tests.logging", "true");

    public static final String VALID_USER_PASSWORD = "Str0ngUs3rP@ssVV0rd";

    private static final AuthenticationApi authenticationApi = new AuthenticationApi(getAuthenticationApi());

    private static ApiClient unauthorizedGuestClient;

    private static final UserApi userApi = new UserApi(getUserGuestClient());

    private static final CooperationApi cooperationApi = new CooperationApi(getUserGuestClient());

    private static ApiClient authorizedCoopAdminClient;

    private static final HouseApi houseApi = new HouseApi(getCooperationAdminClient());

    private static final ApartmentApi apartmentApi = new ApartmentApi(getCooperationAdminClient());

    private static ApiClient authorizedOwnerAdminClient;


    public static com.softserveinc.ita.homeproject.homeoauthserver.ApiClient getAuthenticationApi() {
        com.softserveinc.ita.homeproject.homeoauthserver.ApiClient oauthClient =
            new com.softserveinc.ita.homeproject.homeoauthserver.ApiClient();
        AuthenticationApi authenticationApi = new AuthenticationApi();
        setOauthLoggingFeature(oauthClient);
        authenticationApi.setApiClient(oauthClient);
        setServers(oauthClient);

        return oauthClient;
    }

    @SneakyThrows
    static String getAuthentication(ReadUser readUser) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.email(readUser.getEmail())
            .password(VALID_USER_PASSWORD);
        CreateToken token = authenticationApi.authenticateUser(userCredentials);

        return token.getAccessToken();
    }

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
        client.setBearerToken(getAuthentication(cooperationAdmin));
        setServers(client);
        return client;
    }

    private static ApiClient generateOwnerUserClient() {
        ReadUser ownerUser = createOwnerUser();
        ApiClient client = new ApiClient();
        setLoggingFeature(client);
        client.setBearerToken(getAuthentication(ownerUser));
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
    static ReadUser createCooperationAdmin() {
        CreateCooperation coop = createBaseCooperation();
        CreateUser user = createBaseUser();
        return createCooperationAdmin(cooperationApi, coop, userApi, user);
    }

    @SneakyThrows
    public static ReadUser createCooperationAdmin(CooperationApi cooperationApi, CreateCooperation coop,
                                                  UserApi userApi, CreateUser user) {
        cooperationApi.createCooperation(coop);
        String email = coop.getAdminData().getEmail();

        ResponseEmailItem letter = MailUtil.waitLetterForEmail(email);

        user.setRegistrationToken(MailUtil.getToken(letter));
        user.setEmail(email);
        return userApi.createUser(user);
    }

    private static CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
            .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminData(new CreateCooperationAdminData()
                .email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com")))
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
            .password(VALID_USER_PASSWORD)
            .email("test.receive.apartment@gmail.com")
            .phoneNumber("380501112233");
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
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createBaseCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        apartmentApi.createApartment(createdHouse.getId(), createApartment);

        CreateUser expectedUser = createBaseUser();
        String email = Objects.requireNonNull(createApartment.getInvitations()).get(0).getEmail();
        ResponseEmailItem letter = MailUtil.waitLetterForEmail(email);
        expectedUser.setRegistrationToken(MailUtil.getToken(letter));
        expectedUser.setEmail(email);

        return userApi.createUser(expectedUser);
    }

    private static CreateHouse createHouse() {
        return new CreateHouse()
            .adjoiningArea(500)
            .houseArea(BigDecimal.valueOf(500.0))
            .quantityFlat(50)
            .address(createAddress());
    }


    private static CreateApartment createApartment(int numberOfApartamentInvitations) {
        return new CreateApartment()
            .area(BigDecimal.valueOf(72.5))
            .number("15")
            .invitations(createApartmentInvitation(numberOfApartamentInvitations));
    }

    private static List<CreateInvitation> createApartmentInvitation(int numberOfInvitations) {
        return Stream.generate(CreateApartmentInvitation::new)
            .map(x -> x.apartmentId(APARTMENT_ID).email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .type(InvitationType.APARTMENT))
            .limit(numberOfInvitations)
            .collect(Collectors.toList());
    }

    static void setLoggingFeature(ApiClient client) {
        if (Boolean.parseBoolean(VERBOSE_LOGGING)) {
            Logger logger = Logger.getLogger(ApiClient.class.getName());
            client.getHttpClient()
                .register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));
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

    @SneakyThrows
    public static String getErrorMessage(ApiException apiException) {
        return new ObjectMapper().readValue(apiException.getMessage(), ApiError.class).getErrorMessage();
    }
}
