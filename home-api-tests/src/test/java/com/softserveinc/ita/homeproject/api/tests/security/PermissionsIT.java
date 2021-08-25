package com.softserveinc.ita.homeproject.api.tests.security;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PermissionsIT {

    private final static CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getAdminClient());

    private final static CooperationContactApi cooperationContactApi = new CooperationContactApi(ApiClientUtil.getAdminClient());

    private final static HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final static ApartmentApi apartmentApiCooperationAdmin = new ApartmentApi(ApiClientUtil.getCooperationAdminClient());

    private final static ApartmentInvitationApi apartmentInvitationApiCooperationAdmin =
            new ApartmentInvitationApi(ApiClientUtil.getCooperationAdminClient());

    private final static ApartmentOwnershipApi apartmentOwnershipApiCooperationAdmin =
            new ApartmentOwnershipApi(ApiClientUtil.getCooperationAdminClient());

    private final static PollQuestionApi pollQuestionApiCooperationAdmin =
            new PollQuestionApi(ApiClientUtil.getCooperationAdminClient());

    private final static UserApi userApi = new UserApi(ApiClientUtil.getAdminClient());

    private final static NewsApi newsApiAdmin = new NewsApi(ApiClientUtil.getAdminClient());

    private final static ContactApi contactApi = new ContactApi(ApiClientUtil.getAdminClient());

    private final static CooperationPollApi cooperationPollApiCooperationAdmin
            = new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

    //    private final static ContactApi contactApi = new ContactApi(ApiClientUtil.getAdminClient());

    private final static Long MIN_POLL_DURATION_IN_DAYS = 2L;


    private static final long TEST_OWNERSHIP_ID = 10000001L;

    private static final long TEST_OWNERSHIP_APARTMENT_ID = 100000000L;

    private static final long TEST_DELETE_OWNERSHIP_ID = 10000004L;

    private static final long TEST_DELETE_OWNERSHIP_APARTMENT_ID = 100000001L;


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

/*// UserAPI
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

                            try {
                                return contactApi.queryContactsOnUserWithHttpInfo(
                                        0L, 1, 10, "id,asc",
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
                        true, true, true, false),
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

                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
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
                                return new ApiResponse<ReadCooperation>(401, null);
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
                            ReadContact savedEmailContact = saveContactInCooperation(createEmailContact(), expectedCoop);

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

                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll readPoll = createAndReadCooperationPoll(readCooperation);

                            try {
                                return cooperationPollApi.getCooperationPollWithHttpInfo(
                                        readCooperation.getId(), readPoll.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadPoll>(403, null);
                            }
                        },
                        "get Cooperation Poll",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);

                            try {
                                return cooperationApi.queryCooperationWithHttpInfo(
                                        1, 10, "id,asc", null,
                                        null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "query Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);

                            try {
                                return houseApi.queryHouseWithHttpInfo(
                                        0L, 1, 10, "id,asc", null,
                                        null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "query House",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);

                            try {
                                return cooperationContactApi.queryContactsOnCooperationWithHttpInfo(
                                        0L, 1, 10, "id,asc", null,
                                        null, null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(401, null);
                            }
                        },
                        "query Contacts On Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);

                            try {
                                return cooperationPollApi.queryCooperationPollWithHttpInfo(
                                        0L, 1, 10, "id,asc", null,
                                        null, null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Cooperation Poll",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);
                            ReadCooperation createdCooperation = createAndReadBaseCooperation();

                            try {
                                return cooperationApi.deleteCooperationWithHttpInfo(createdCooperation.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Cooperation",
                        true, false, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            try {
                                return houseApi.deleteHouseWithHttpInfo(expectedCoop.getId(), expectedHouse.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete House",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadContact savedEmailContact = saveContactInCooperation(createEmailContact(), expectedCoop);

                            try {
                                return cooperationContactApi.deleteContactOnCooperationWithHttpInfo(
                                        expectedCoop.getId(), savedEmailContact.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Contact On Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll expectedPoll = createAndReadCooperationPoll(readCooperation);

                            try {
                                return cooperationPollApi.deleteCooperationPollWithHttpInfo(
                                        readCooperation.getId(), expectedPoll.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Cooperation Poll",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationApi cooperationApi = new CooperationApi(apiClient);
                            ReadCooperation readCooperation = createAndReadBaseCooperation();
                            UpdateCooperation updateCoop = getUpdateCooperation();

                            try {
                                return cooperationApi.updateCooperationWithHttpInfo(
                                        readCooperation.getId(), updateCoop);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            HouseApi houseApi = new HouseApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            UpdateHouse updateHouse = updateHouse();

                            try {
                                return houseApi.updateHouseWithHttpInfo(
                                        expectedCoop.getId(), expectedHouse.getId(), updateHouse);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update House",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationContactApi cooperationContactApi = new CooperationContactApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadContact savedEmailContact = saveContactInCooperation(createEmailContact(), expectedCoop);
                            UpdateContact updateEmailContact = updateEmailContact();

                            try {
                                return cooperationContactApi.updateContactOnCooperationWithHttpInfo(
                                        expectedCoop.getId(), savedEmailContact.getId(), updateEmailContact);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Contact On Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            CooperationPollApi cooperationPollApi = new CooperationPollApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll readPoll = createAndReadCooperationPoll(readCooperation);
                            UpdatePoll updatePoll = updatePoll();

                            try {
                                return cooperationPollApi.updateCooperationPollWithHttpInfo(
                                        readCooperation.getId(), readPoll.getId(), updatePoll);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Cooperation Poll",
                        false, true, false, false),
// HouseApiImpl // ↑↑↑ all right
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            CreateApartment createApartment = createApartment();

                            try {
                                return apartmentApi.createApartmentWithHttpInfo(
                                        expectedHouse.getId(), createApartment);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create Apartment",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            CreateApartment createApartment = createApartment();
                            ReadApartment createdApartment = createApartment(expectedHouse, createApartment);

                            try {
                                return apartmentApi.deleteApartmentWithHttpInfo(
                                        expectedHouse.getId(), createdApartment.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Apartment",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            CreateApartment createApartment = createApartment();
                            ReadApartment createdApartment = createApartment(expectedHouse, createApartment);

                            try {
                                return apartmentApi.getApartmentWithHttpInfo(
                                        expectedHouse.getId(), createdApartment.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Apartment",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);

                            try {
                                return apartmentApi.queryApartmentWithHttpInfo(
                                        1L, 1,10, "id,asc", null,
                                        null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Apartment",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentApi apartmentApi = new ApartmentApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            CreateApartment createApartment = createApartment();
                            ReadApartment createdApartment = createApartment(expectedHouse, createApartment);
                            UpdateApartment updateApartment = updateApartment();

                            try {
                                return apartmentApi.updateApartmentWithHttpInfo(
                                        expectedHouse.getId(), createdApartment.getId(), updateApartment);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Apartment",
                        false, true, false, false),
// ApartmentApiImpl // ↑↑↑ all right
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            CreateApartment createApartment = createApartment();

                            ReadApartment readApartment = createApartment(expectedHouse, createApartment);
                            CreateApartmentInvitation createApartmentInvitation = createApartmentInvitation();

                            try {
                                return apartmentInvitationApi.createInvitationWithHttpInfo(
                                        readApartment.getId(), createApartmentInvitation);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(403, null);
                            }
                        },
                        "create Invitation",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);

                            CreateApartment createApartment = createApartmentWithoutInvitation();
                            ReadApartment readApartment = createApartment(expectedHouse, createApartment);

                            CreateApartmentInvitation createApartmentInvitation = createApartmentInvitation();
                            ReadApartmentInvitation readApartmentInvitation =
                                    createInvitation(readApartment, createApartmentInvitation);

                            try {
                                return apartmentInvitationApi.deleteInvitationWithHttpInfo(
                                        readApartment.getId(), readApartmentInvitation.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(403, null);
                            }
                        },
                        "delete Invitation",
                        false, true, false, false),
                // TODO not stable, work only once after sql script
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);

                            try {
                                return apartmentOwnershipApi.deleteOwnershipWithHttpInfo(
                                        TEST_DELETE_OWNERSHIP_APARTMENT_ID, TEST_DELETE_OWNERSHIP_ID);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(403, null);
                            }
                        },
                        "delete Ownership",
                        false, true, false, false),*/
                // TODO error
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);

                            CreateApartment createApartment = createApartmentWithoutInvitation();
                            ReadApartment readApartment = createApartment(expectedHouse, createApartment);

                            CreateApartmentInvitation createApartmentInvitation = createApartmentInvitation();
                            ReadApartmentInvitation readApartmentInvitation =
                                    createInvitation(readApartment, createApartmentInvitation);

                            try {
                                return apartmentInvitationApi.getInvitationWithHttpInfo(
                                        readApartment.getId(), readApartmentInvitation.getId());
                            } catch (ApiException e) {
                                System.out.println("-->" + e.getCode());
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Invitation",
                        false, true, false, false),

/*
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);

                            try {
                                return apartmentOwnershipApi.getOwnershipWithHttpInfo(
                                        TEST_OWNERSHIP_APARTMENT_ID, TEST_OWNERSHIP_ID);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Ownership",
                        false, true, true, false),*/
                // TODO error2
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);

                            try {
                                return apartmentInvitationApi.queryInvitationWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Invitation",
                        false, true, false, false),/*
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);

                            try {
                                return apartmentOwnershipApi.queryOwnershipWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Ownership",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(apiClient);
                            ReadCooperation expectedCoop = createAndReadBaseCooperation();
                            ReadHouse expectedHouse = createAndReadHouse(expectedCoop);
                            CreateApartment createApartment = createApartmentWithoutInvitation();
                            ReadApartment readApartment = createApartment(expectedHouse, createApartment);
                            CreateApartmentInvitation createApartmentInvitation = createApartmentInvitation();
                            ReadApartmentInvitation readApartmentInvitation =
                                    createInvitation(readApartment, createApartmentInvitation);

                            UpdateApartmentInvitation updateApartmentInvitation = updateApartmentInvitation();

                            try {
                                return apartmentInvitationApi.updateInvitationWithHttpInfo(
                                        readApartment.getId(), readApartmentInvitation.getId(), updateApartmentInvitation);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Invitation",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            ApartmentOwnershipApi apartmentOwnershipApi = new ApartmentOwnershipApi(apiClient);
                            UpdateOwnership updateOwnership = new UpdateOwnership()
                                    .ownershipPart(BigDecimal.valueOf(0.5));

                            try {
                                return apartmentOwnershipApi.updateOwnershipWithHttpInfo(
                                        TEST_OWNERSHIP_APARTMENT_ID, TEST_OWNERSHIP_ID, updateOwnership
                                );
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Ownership",
                        false, true, false, false),

// NewsApiImpl
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);
                            CreateNews expectedNews = createNews();

                            try {
                                return newsApi.createNewsWithHttpInfo(expectedNews);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create News",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);
                            ReadNews readNews = createAndReadNews();

                            try {
                                return newsApi.deleteNewsWithHttpInfo(readNews.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete News",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);

                            try {
                                return newsApi.getAllNewsWithHttpInfo(
                                        1, 1, "id,asc", null,
                                        null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get All News",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);
                            ReadNews readNews = createAndReadNews();

                            try {
                                return newsApi.getNewsWithHttpInfo(readNews.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get News",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            NewsApi newsApi = new NewsApi(apiClient);
                            ReadNews readNews = createAndReadNews();
                            UpdateNews updateNews = updateNews();

                            try {
                                return newsApi.updateNewsWithHttpInfo(readNews.getId(), updateNews);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update News",
                        true, true, false, false),

//PollApiImpl // ↑↑↑ all right
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadHouse expectedHouse = createAndReadHouse(readCooperation);
                            CreatePoll createPoll = createPoll(readCooperation);
                            HouseLookup houseLookup = new HouseLookup().id(expectedHouse.getId());
                            ReadPoll readPoll = readCooperationPoll(readCooperation, createPoll);

                            try {
                                return polledHouseApi.createPolledHouseWithHttpInfo(readPoll.getId(), houseLookup);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create Polled House",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll readPoll = createAndReadCooperationPoll(readCooperation);
                            CreateQuestion createQuestion = createAdviceQuestion();

                            try {
                                return pollQuestionApi.createQuestionWithHttpInfo(readPoll.getId(), createQuestion);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "create Question",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadHouse expectedHouse = createAndReadHouse(readCooperation);
                            CreatePoll createPoll = createPoll(readCooperation);
                            ReadPoll readPoll = readCooperationPoll(readCooperation, createPoll);

                            try {
                                return polledHouseApi.deletePolledHouseWithHttpInfo(
                                        readPoll.getId(), expectedHouse.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Polled House",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll readPoll = createAndReadCooperationPoll(readCooperation);
                            CreateQuestion createQuestion = createAdviceQuestion();
                            ReadQuestion createdQuestion = createdQuestion(readPoll, createQuestion);

                            try {
                                return pollQuestionApi.deleteQuestionWithHttpInfo(
                                        readPoll.getId(), createdQuestion.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "delete Question",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollApi pollApi = new PollApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll readPoll = createAndReadCooperationPoll(readCooperation);
                            try {
                                return pollApi.getPollWithHttpInfo(readPoll.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Poll",
                        false, true, true, false),*/
                // TODO debug
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadHouse expectedHouse = createAndReadHouse(readCooperation);
                            CreatePoll createPoll = createPoll(readCooperation);
                            ReadPoll readPoll = readCooperationPoll(readCooperation, createPoll);

                            try {
                                return polledHouseApi.getPolledHouseWithHttpInfo(
                                        readPoll.getId(), expectedHouse.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Polled House",
                        false, true, true, false)/*,

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll readPoll = createAndReadCooperationPoll(readCooperation);
                            CreateQuestion createQuestion = createAdviceQuestion();
                            ReadQuestion readQuestion = createdQuestion(readPoll, createQuestion);

                            try {
                                return pollQuestionApi.getQuestionWithHttpInfo(
                                        readPoll.getId(), readQuestion.getId());
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "get Question",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollApi pollApi = new PollApi(apiClient);

                            try {
                                return pollApi.queryPollWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Poll",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PolledHouseApi polledHouseApi = new PolledHouseApi(apiClient);

                            try {
                                return polledHouseApi.queryPolledHouseWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Polled House",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);

                            try {
                                return pollQuestionApi.queryQuestionWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "query Question",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            PollQuestionApi pollQuestionApi = new PollQuestionApi(apiClient);
                            ReadCooperation readCooperation = createAndReadCooperationForPoll();
                            ReadPoll readPoll = createAndReadCooperationPoll(readCooperation);
                            CreateQuestion createQuestion = createAdviceQuestion();
                            ReadQuestion readQuestion = createdQuestion(readPoll, createQuestion);
                            UpdateQuestion updateQuestion = updateQuestion();

                            try {
                                return pollQuestionApi.updateQuestionWithHttpInfo(
                                        readPoll.getId(), readQuestion.getId(), updateQuestion);
                            } catch (ApiException e) {
                                return new ApiResponse<ReadContact>(403, null);
                            }
                        },
                        "update Question",
                        false, true, false, false)*/

        );
    }

    private static UpdateQuestion updateQuestion() {
        return new UpdateQuestion()
                .question("Do you like updated question?")
                .type(QuestionType.ADVICE);
    }

    @SneakyThrows
    private static ReadQuestion createdQuestion(ReadPoll readPoll, CreateQuestion createQuestion) {
        return pollQuestionApiCooperationAdmin.createQuestion(
                readPoll.getId(), createQuestion);
    }

    private static CreateQuestion createAdviceQuestion() {
        return new CreateAdviceQuestion()
                .question("Your proposal for your house?")
                .type(QuestionType.ADVICE);
    }

    private static UpdateNews updateNews() {
        return new UpdateNews()
                .title("UpdatedTitle")
                .description("UpdatedDescription")
                .text("UpdatedText")
                .photoUrl("http://updatedurl.example.com")
                .source("UpdatedSource");
    }

    @SneakyThrows
    private static ReadNews createAndReadNews() {
        return newsApiAdmin.createNews(createNews().title("FirstTitle"));
    }

    private static CreateNews createNews() {
        return new CreateNews()
                .title("Title")
                .description("Description")
                .text("Text")
                .photoUrl("http://someurl.example.com")
                .source("Source");
    }

    @SneakyThrows
    private static ReadApartmentInvitation createInvitation(
            ReadApartment readApartment, CreateApartmentInvitation createApartmentInvitation) {
        return apartmentInvitationApiCooperationAdmin.
                createInvitation(readApartment.getId(), createApartmentInvitation);
    }

    private static UpdateApartment updateApartment() {
        return new UpdateApartment()
                .number("27")
                .area(BigDecimal.valueOf(32.1));
    }

    @SneakyThrows
    private static ReadApartment createApartment(ReadHouse expectedHouse, CreateApartment createApartment) {
        return apartmentApiCooperationAdmin.createApartment(expectedHouse.getId(), createApartment);
    }

    private static List<CreateInvitation> createListOfInvitation() {
        List<CreateInvitation> createInvitations = new ArrayList<>();
        createInvitations.add(new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.1))
                .email("test.receive.messages@gmail.com")
                .type(InvitationType.APARTMENT));

        createInvitations.add(new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.1))
                .email("test.receive.messages@gmail.com")
                .type(InvitationType.APARTMENT));

        return createInvitations;
    }

    private static CreateApartmentInvitation createApartmentInvitation() {
        return (CreateApartmentInvitation) new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.1))
                .email("test.receive.messages@gmail.com")
                .type(InvitationType.APARTMENT);
    }

    private static UpdateApartmentInvitation updateApartmentInvitation() {
        return new UpdateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(1))
                .email("test.receive.messages@gmail.com");
    }

    private static CreateApartment createApartment(List<CreateInvitation> createInvitations) {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15")
                .invitations(createInvitations);
    }

    private static CreateApartment createApartmentWithoutInvitation() {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15");
    }

    private static CreateApartment createApartment() {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15")
                .invitations(createListOfInvitation());
    }

    private static UpdatePoll updatePoll() {
        LocalDateTime completionDate = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plusDays(MIN_POLL_DURATION_IN_DAYS)
                .plusMinutes(1L);
        return new UpdatePoll()
                .header("Updated poll for our houses")
                .completionDate(completionDate)
                .status(PollStatus.SUSPENDED);
    }

    private static UpdateHouse updateHouse() {
        return new UpdateHouse()
                .adjoiningArea(2000)
                .houseArea(BigDecimal.valueOf(2000.0))
                .quantityFlat(200)
                .address(new Address()
                        .region("upd")
                        .city("upd")
                        .district("upd")
                        .street("upd")
                        .houseBlock("upd")
                        .houseNumber("upd")
                        .zipCode("upd"));
    }

    private static UpdateCooperation getUpdateCooperation() {
        return new UpdateCooperation()
                .name("upd")
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .address(new Address()
                        .city("upd")
                        .district("upd")
                        .houseBlock("upd")
                        .houseNumber("upd")
                        .region("upd")
                        .street("upd")
                        .zipCode("upd"));
    }


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

    @SneakyThrows
    private static ReadCooperation createAndReadBaseCooperation() {
        return cooperationApi.createCooperation(createBaseCooperation());
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
    private static ReadCooperation createAndReadCooperationForPoll() {
        return cooperationApi.createCooperationWithHttpInfo(createCooperationForPoll()).getData();
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
    private static ReadPoll createAndReadCooperationPoll(ReadCooperation readCooperation) {
        return cooperationPollApiCooperationAdmin.createCooperationPoll(
                readCooperation.getId(), createPoll(readCooperation));
    }

    @SneakyThrows
    private static ReadPoll readCooperationPoll(ReadCooperation readCooperation, CreatePoll createPoll) {
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
