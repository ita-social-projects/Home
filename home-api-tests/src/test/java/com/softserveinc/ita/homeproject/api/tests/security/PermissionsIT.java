package com.softserveinc.ita.homeproject.api.tests.security;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.*;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.model.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PermissionsIT {

    private final static CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getAdminClient());

    private final static CooperationContactApi cooperationContactApi = new CooperationContactApi(ApiClientUtil.getAdminClient());

    private final static HouseApi houseApi = new HouseApi(ApiClientUtil.getAdminClient());

    private final static UserApi userApi = new UserApi(ApiClientUtil.getAdminClient());

    private final static ContactApi contactApi = new ContactApi(ApiClientUtil.getAdminClient());

    private final static CooperationPollApi cooperationPollApiCooperationAdmin
            = new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

    //    private final static ContactApi contactApi = new ContactApi(ApiClientUtil.getAdminClient());

    private final static Long MIN_POLL_DURATION_IN_DAYS = 2L;


    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testAdmin(Function<ApiClient, ApiResponse<?>> action, String x, boolean admin) {

        int statusCode = getStatusCode(action, ApiClientUtil.getAdminClient());
        checkAuthenticatedUser(admin, statusCode);
    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testCooperationAdmin(Function<ApiClient, ApiResponse<?>> action, String x, boolean admin, boolean coopAdmin) {

        int statusCode = getStatusCode(action, ApiClientUtil.getCooperationAdminClient());
        checkAuthenticatedUser(coopAdmin, statusCode);

    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testOwner(Function<ApiClient, ApiResponse<?>> action, String x, boolean admin, boolean coopAdmin, boolean owner) {
        int statusCode = getStatusCode(action, ApiClientUtil.getOwnerClient());
        checkAuthenticatedUser(owner, statusCode);
    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testGuestUser(Function<ApiClient, ApiResponse<?>> action, String x,
                       boolean admin, boolean coopAdmin, boolean owner, boolean unauthenticated) {

        int statusCode = getStatusCode(action, ApiClientUtil.getUnauthorizedUserClient());
        checkUnauthenticatedUser(unauthenticated, statusCode);

    }

    static Stream<Arguments> check() {

        return Stream.of(
// UserAPI
/*

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ContactApi contactApi = new ContactApi(apiClient);
                            CreateContact createEmailContact = createEmailContact();
                            ReadUser expectedUser = createAndReadTestUserViaInvitation();

                            try {
                                return contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(), createEmailContact);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "create Contact On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            UserApi userApi = new UserApi(apiClient);
                            CreateUser createUser = createBaseUserViaInvitation();

                            try {
                                return userApi.createUserWithHttpInfo(createUser);
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                            return null;
                        },
                        "create User",
                        true, true, true, true),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ReadUser readUser = createAndReadTestUserViaInvitation();
                            UserApi userApi = new UserApi(apiClient);

                            try {
                                return userApi.getUserWithHttpInfo(readUser.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadUser>(401, null);
                            }
                        },
                        "get User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ContactApi contactApi = new ContactApi(apiClient);
                            ReadUser expectedUser = createAndReadTestUserViaInvitation();

                            try {
                                return contactApi.queryContactsOnUserWithHttpInfo(
                                        expectedUser.getId(), 1, 10, "id,asc",
                                        null, null, null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }

                        },
                        "query Contacts On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ContactApi contactApi = new ContactApi(apiClient);
                            ReadUser expectedUser = createAndReadTestUserViaInvitation();

                            ReadContact savedPhoneContact = createContact(expectedUser.getId(), createPhoneContact());

                            try {
                                return contactApi.deleteContactOnUserWithHttpInfo(expectedUser.getId(),
                                        savedPhoneContact.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(401, null);
                            }

                        },
                        "delete Contact On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            UserApi userApi = new UserApi(apiClient);

                            try {
                                return userApi.getAllUsersWithHttpInfo(1, 10, "id,asc",
                                        null, null, null, null, null, null,
                                        null);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadUser>(401, null);
                            }
                        },
                        "get All Users",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ContactApi contactApi = new ContactApi(apiClient);
                            ReadUser expectedUser = createAndReadTestUserViaInvitation();
                            CreateContact createEmailContact = createEmailContact();

                            ReadContact savedEmailContact;
                            try {
                                savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }

                            try {
                                return contactApi.getContactOnUserWithHttpInfo(expectedUser.getId(),
                                        savedEmailContact.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }

                        },
                        "get Contact On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ContactApi contactApi = new ContactApi(apiClient);
                            ReadUser expectedUser = createAndReadTestUserViaInvitation();
                            CreateContact createEmailContact = createEmailContact();
                            UpdateContact updateEmailContact = updateEmailContact();

                            ReadContact savedEmailContact;
                            try {
                                savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                            try {
                                return contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
                                        savedEmailContact.getId(), updateEmailContact);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "update Contact OnUser",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            UserApi userApi = new UserApi(apiClient);
                            ReadUser savedUser = createAndReadTestUserViaInvitation();
                            UpdateUser updateUser = getUpdateUser();

                            try {
                                return userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadUser>(401, null);
                            }
                        },
                        "update User",
                        true, true, true, false),*/
// CooperationApi // ↑↑↑ all right
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);
                            CreateCooperation createCooperation = createBaseCooperation();

                            try {
                                return cooperationApi.createCooperationWithHttpInfo(createCooperation);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadCooperation>(403, null);
                            }
                        },
                        "create Cooperation",
                        true, false, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);
                            CreateHouse createHouse = createHouse();
                            ReadCooperation createdCooperation = createAndReadBaseCooperation();

                            try {
                                return houseApi.createHouseWithHttpInfo(createdCooperation.getId(), createHouse);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadCooperation>(403, null);
                            }
                        },
                        "create House",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);
                            ReadCooperation createdCooperation = createAndReadBaseCooperation();
                            CreateContact createEmailContact = createEmailContact();

                            try {
                                return cooperationContactApi.createContactOnCooperationWithHttpInfo(
                                        createdCooperation.getId(), createEmailContact);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadCooperation>(403, null);
                            }
                        },
                        "create Contact On Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);

                            CreateCooperation createCooperation = createCooperationForPoll();
                            ReadCooperation readCooperation = readCooperation(createCooperation);
                            CreatePoll createPoll = createPoll(readCooperation);

                            try {
                                return cooperationPollApi.createCooperationPollWithHttpInfo(
                                        readCooperation.getId(), createPoll);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadCooperation>(403, null);
                            }
                        },
                        "create Cooperation Poll",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);
                            ReadCooperation createdCooperation = createAndReadBaseCooperation();

                            try {
                                return cooperationApi.getCooperationWithHttpInfo(createdCooperation.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadCooperation>(403, null);
                            }
                        },
                        "get Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);

                            try {
                                return houseApi.getHouseWithHttpInfo(expectedCoop.getId(), expectedHouse.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadHouse>(401, null);
                            }
                        },
                        "get House",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            CreateContact createEmailContact = createEmailContact();
                            ReadContact savedEmailContact = saveContactInCooperation(createEmailContact, expectedCoop);

                            try {
                                return cooperationContactApi.getContactOnCooperationWithHttpInfo(
                                        expectedCoop.getId(), savedEmailContact.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "get Contact On Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);

                            CreateCooperation createCooperation = createCooperationForPoll();
                            ReadCooperation readCooperation = readCooperation(createCooperation);
                            CreatePoll createPoll = createPoll(readCooperation);

                            ReadPoll expectedPoll = getCooperationPoll(readCooperation, createPoll);

                            try {
                                return cooperationPollApi.getCooperationPollWithHttpInfo(
                                        readCooperation.getId(), expectedPoll.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadPoll>(403, null);
                            }
                        },
                        "get Cooperation Poll",
                        false, true, true, false)/*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);

                            try {
                                return cooperationApi.queryCooperationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "query Cooperation",
                        true, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);

                            try {
                                return houseApi.queryHouseWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "query House",
                        true, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);

                            try {
                                return cooperationContactApi.queryContactsOnCooperationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "query Contacts On Cooperation",
                        true, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);

                            try {
                                return cooperationPollApi.queryCooperationPollWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Cooperation Poll",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);

                            try {
                                return cooperationApi.deleteCooperationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Cooperation",
                        true, false, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);

                            try {
                                return houseApi.deleteHouseWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete House",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);

                            try {
                                return cooperationContactApi.deleteContactOnCooperationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Contact On Cooperation",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);

                            try {
                                return cooperationPollApi.deleteCooperationPollWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Cooperation Poll",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);

                            try {
                                return cooperationApi.updateCooperationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Cooperation",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);

                            try {
                                return houseApi.updateHouseWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update House",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);

                            try {
                                return cooperationContactApi.updateContactOnCooperationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Contact On Cooperation",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);

                            try {
                                return cooperationContactApi.updateContactOnCooperationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Contact On Cooperation",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);

                            try {
                                return cooperationPollApi.updateCooperationPollWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Cooperation Poll",
                        true, true, false, false)*/

// HouseApiImpl
                /*,
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);

                            try {
                                return apartmentApi.createApartmentWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create Apartment",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);

                            try {
                                return apartmentApi.deleteApartmentWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Apartment",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);

                            try {
                                return apartmentApi.getApartmentWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Apartment",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);

                            try {
                                return apartmentApi.queryApartmentWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Apartment",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);

                            try {
                                return apartmentApi.updateApartmentWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Apartment",
                        false, true, false, false)*//*,

// ApartmentApiImpl
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            try {
                                return apartmentInvitationApi.createInvitationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create Invitation",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            try {
                                return apartmentInvitationApi.deleteInvitationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Invitation",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);

                            try {
                                return apartmentOwnershipApi.deleteOwnershipWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Ownership",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            try {
                                return apartmentInvitationApi.getInvitationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Invitation",
                        false, true, false, false)*//*,


                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);

                            try {
                                return apartmentOwnershipApi.getOwnershipWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Ownership",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            try {
                                return apartmentInvitationApi.queryInvitationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Invitation",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            try {
                                return apartmentInvitationApi.queryInvitationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Invitation",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);

                            try {
                                return apartmentOwnershipApi.queryOwnershipWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Ownership",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            try {
                                return apartmentInvitationApi.updateInvitationWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Invitation",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);

                            try {
                                return apartmentOwnershipApi.updateOwnershipWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Ownership",
                        false, true, false, false),*/

// NewsApiImpl
                /*Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);

                            try {
                                return newsApi.createNewsWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create News",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);

                            try {
                                return newsApi.deleteNewsWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete News",
                        true, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);

                            try {
                                return newsApi.getAllNewsWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get All News",
                        true, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);

                            try {
                                return newsApi.getNewsWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get News",
                        true, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);

                            try {
                                return newsApi.updateNewsWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update News",
                        true, true, false, false),*/

//PollApiImpl
                /*Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);

                            try {
                                return polledHouseApi.createPolledHouseWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create Polled House",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);

                            try {
                                return pollQuestionApi.createQuestionWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create Question",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);

                            try {
                                return polledHouseApi.deletePolledHouseWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Polled House",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);

                            try {
                                return pollQuestionApi.deleteQuestionWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Question",
                        false, true, false, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollApi pollApi = new PollApi(apiClient);

                            try {
                                return pollApi.getPollWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Poll",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);

                            try {
                                return polledHouseApi.getPolledHouseWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Polled House",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);

                            try {
                                return pollQuestionApi.getQuestionWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Question",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollApi pollApi = new PollApi(apiClient);

                            try {
                                return pollApi.queryPollWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Poll",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);

                            try {
                                return polledHouseApi.queryPolledHouseWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Polled House",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);

                            try {
                                return pollQuestionApi.queryQuestionWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Question",
                        false, true, true, false)*//*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);

                            try {
                                return pollQuestionApi.updateQuestionWithHttpInfo();
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Question",
                        false, true, false, false)*/


        );
    }





/*    @Test
    void testUserAdmin() throws ApiException {
        ApiClient apiClient = ApiClientUtil.getAdminClient();
//        System.out.println(apiClient instanceof ApiClientUtil);

//        CooperationApi cooperationApi = new CooperationApi(apiClient);
//        cooperationApi.getCooperation(1L);
    }*/


    private int getStatusCode(Function<ApiClient, ApiResponse<?>> action, ApiClient unauthorizedClient) {
        int statusCode;
        ApiResponse<?> resp = action.apply(unauthorizedClient);
        statusCode = resp.getStatusCode();
        return statusCode;
    }

    public void checkAuthenticatedUser(boolean role, int statusCode) {
        if (role) {
            Assertions.assertNotEquals(Response.Status.UNAUTHORIZED.getStatusCode(), statusCode);
            Assertions.assertNotEquals(Response.Status.FORBIDDEN.getStatusCode(), statusCode);
        } else {
            Assertions.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), statusCode);
        }
    }

    public void checkUnauthenticatedUser(boolean unauthenticatedPermission, int statusCode) {
        if (unauthenticatedPermission) {
            Assertions.assertNotEquals(Response.Status.FORBIDDEN.getStatusCode(), statusCode);
        } else {
            if (statusCode == 401) {
                Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), statusCode);
            } else {
                Assertions.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), statusCode);
            }
        }
    }


    //--------------------------------
    @SneakyThrows
    private static CreateUser createBaseUserViaInvitation() {
        CreateCooperation createCoop = createBaseCooperation();
        cooperationApi.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse, createCoop.getAdminEmail())));
        expectedUser.setEmail(createCoop.getAdminEmail());
        return expectedUser;
    }

    @SneakyThrows
    private static ReadUser createAndReadTestUserViaInvitation() {
        return userApi.createUser(createBaseUserViaInvitation());
    }

    private static CreateContact createEmailContact() {
        return new CreateEmailContact()
                .email("newEmailContact@example.com")
                .main(false)
                .type(ContactType.EMAIL);
    }

    @SneakyThrows
    private static ReadContact createContact(Long id, CreateContact createContact) {
        return contactApi.createContactOnUser(id, createContact);
    }

    @SneakyThrows
    private static ReadContact saveContactInCooperation(CreateContact createContact, ReadCooperation expectedCoop) {
        return cooperationContactApi.createContactOnCooperation(expectedCoop.getId(), createContact);
    }

    private static UpdateContact updateEmailContact() {
        return new UpdateEmailContact()
                .email("updatedEmailContact@example.com")
                .main(false)
                .type(ContactType.EMAIL);
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

    private static CreateCooperation createCooperationForPoll() {
        return new CreateCooperation()
                .name("newCooperationTest")
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(RandomStringUtils.randomAlphabetic(12).concat("@gmail.com"))
                .address(createAddress())
                .addHousesItem(createHouse())
                .addHousesItem(createHouse())
                .addHousesItem(createHouse());
    }

    @SneakyThrows
    private static ReadCooperation createAndReadBaseCooperation() {
        return cooperationApi.createCooperation(createBaseCooperation());
    }

    private static CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("test.receive.apartment@gmail.com");
    }

    private static UpdateUser getUpdateUser() {
        return new UpdateUser()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
                .password("somePassword");
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

    private static CreateHouse createHouse() {
        return new CreateHouse()
                .adjoiningArea(500)
                .houseArea(BigDecimal.valueOf(500.0))
                .quantityFlat(50)
                .address(createAddress());
    }

    @SneakyThrows
    private static ReadHouse createAndReadHouse(ReadCooperation readCooperation) {
        return houseApi.createHouse(readCooperation.getId(), createHouse());
    }

    @SneakyThrows
    private static ReadCooperation readCooperation(CreateCooperation createCooperation) {
        return cooperationApi.createCooperationWithHttpInfo(createCooperation).getData();
    }

    @SneakyThrows
    private static CreatePoll createPoll(ReadCooperation readCooperation) {

        LocalDateTime completionDate = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plusDays(MIN_POLL_DURATION_IN_DAYS)
                .plusMinutes(1L);
        return new CreatePoll()
                .header("Poll for our houses")
                .type(PollType.SIMPLE)
                .completionDate(completionDate)
                .addHousesItem(new HouseLookup().id(readCooperation.getHouses().get(0).getId()))
                .addHousesItem(new HouseLookup().id(readCooperation.getHouses().get(1).getId()));
    }

    @SneakyThrows
    private static ReadPoll getCooperationPoll(ReadCooperation readCooperation, CreatePoll createPoll) {
        return cooperationPollApiCooperationAdmin.createCooperationPoll(
                readCooperation.getId(), createPoll);
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

    private static String getToken(String str) {
        Pattern pattern = Pattern.compile("(?<=:) .* (?=<)");
        Matcher matcher = pattern.matcher(str);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }

        return result.trim();
    }

    private static CreateContact createPhoneContact() {
        return new CreatePhoneContact()
                .phone("+380632121212")
                .main(false)
                .type(ContactType.PHONE);
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
}
