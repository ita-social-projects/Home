package com.softserveinc.ita.homeproject.api.tests.security;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.*;
import com.softserveinc.ita.homeproject.client.model.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class PermissionsIT {

    private final static CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getAdminClient());

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testAdmin(Function<ApiClient, ApiResponse<?>> action, String x, boolean admin) {

        int statusCode = getStatusCode(action, ApiClientUtil.getAdminClient());
        checkAuthenticatedUser(admin, statusCode);
    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testCooperationAdmin(Function<ApiClient, ApiResponse<?>> action, String x,
                              boolean admin, boolean coopAdmin) {

        int statusCode = getStatusCode(action, ApiClientUtil.getCooperationAdminClient());
        checkAuthenticatedUser(coopAdmin, statusCode);
    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testOwner(Function<ApiClient, ApiResponse<?>> action, String x,
                   boolean admin, boolean coopAdmin, boolean owner) {
        int statusCode = getStatusCode(action, ApiClientUtil.getOwnerClient());
        checkAuthenticatedUser(owner, statusCode);
    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testGuestUser(Function<ApiClient, ApiResponse<?>> action, String x,
                       boolean admin, boolean coopAdmin, boolean owner, boolean guestUser) {

        int statusCode = getStatusCode(action, ApiClientUtil.getUnauthorizedUserClient());
        checkUnauthenticatedUser(guestUser, statusCode);
    }

    static Stream<Arguments> check() {

        return Stream.of(

// UserAPI
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ContactApi(apiClient).createContactOnUserWithHttpInfo(
                                        1L, new CreateContact());
                            } catch (ApiException e) {
                                return new ApiResponse<>(e.getCode(), null);
                            }
                        },
                        "create Contact On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new UserApi(apiClient).createUserWithHttpInfo(new CreateUser());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create User",
                        true, true, true, true),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new UserApi(apiClient).getUserWithHttpInfo(1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ContactApi(apiClient).queryContactsOnUserWithHttpInfo(
                                        1L, 1, 10, "id,asc",
                                        null, null, null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Contacts On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ContactApi(apiClient).deleteContactOnUserWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Contact On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new UserApi(apiClient).getAllUsersWithHttpInfo(
                                        1, 10, "id,asc", null, null,
                                        null, null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get All Users",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ContactApi(apiClient).getContactOnUserWithHttpInfo(1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Contact On User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new UserApi(apiClient).deleteUserWithHttpInfo(1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete User",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ContactApi(apiClient).updateContactOnUserWithHttpInfo(
                                        1L, 1L, new UpdateContact());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Contact OnUser",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new UserApi(apiClient).updateUserWithHttpInfo(
                                        1L, new UpdateUser());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update User",
                        true, true, true, false),

// CooperationApi
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationApi(apiClient).createCooperationWithHttpInfo(
                                        createBaseCooperation());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create Cooperation",
                        true, false, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new HouseApi(apiClient).createHouseWithHttpInfo(
                                        1L, createHouse());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create House",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationContactApi(apiClient).createContactOnCooperationWithHttpInfo(
                                        1L, createEmailContact());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create Contact On Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationPollApi(apiClient).createCooperationPollWithHttpInfo(
                                        1L, createPoll(createAndReadCooperationForPoll()));
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create Cooperation Poll",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationApi(apiClient).getCooperationWithHttpInfo(1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new HouseApi(apiClient).getHouseWithHttpInfo(1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get House",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationContactApi(apiClient).getContactOnCooperationWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Contact On Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationPollApi(apiClient).getCooperationPollWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Cooperation Poll",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationApi(apiClient).queryCooperationWithHttpInfo(
                                        1, 10, "id,asc", null,
                                        null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new HouseApi(apiClient).queryHouseWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query House",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationContactApi(apiClient).queryContactsOnCooperationWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Contacts On Cooperation",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationPollApi(apiClient).queryCooperationPollWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Cooperation Poll",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationApi(apiClient).deleteCooperationWithHttpInfo(
                                        1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Cooperation",
                        true, false, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new HouseApi(apiClient).deleteHouseWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete House",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationContactApi(apiClient).deleteContactOnCooperationWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Contact On Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationPollApi(apiClient).deleteCooperationPollWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Cooperation Poll",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationApi(apiClient).updateCooperationWithHttpInfo(
                                        1L, updateCooperation());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new HouseApi(apiClient).updateHouseWithHttpInfo(
                                        1L, 1L, updateHouse());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update House",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationContactApi(apiClient).updateContactOnCooperationWithHttpInfo(
                                        1L, 1L, updateEmailContact());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Contact On Cooperation",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new CooperationPollApi(apiClient).updateCooperationPollWithHttpInfo(
                                        1L, 1L, updatePoll());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Cooperation Poll",
                        false, true, false, false),

// HouseApiImpl // ↑↑↑ all right
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentApi(apiClient).createApartmentWithHttpInfo(
                                        1L, createApartment());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create Apartment",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentApi(apiClient).deleteApartmentWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Apartment",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentApi(apiClient).getApartmentWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Apartment",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentApi(apiClient).queryApartmentWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Apartment",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentApi(apiClient).updateApartmentWithHttpInfo(
                                        1L, 1L, updateApartment());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Apartment",
                        false, true, false, false),

// ApartmentApiImpl // ↑↑↑ all right
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentInvitationApi(apiClient).createInvitationWithHttpInfo(
                                        1L, createApartmentInvitation());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create Invitation",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentInvitationApi(apiClient).deleteInvitationWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Invitation",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentOwnershipApi(apiClient).deleteOwnershipWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Ownership",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentInvitationApi(apiClient).getInvitationWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Invitation",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentOwnershipApi(apiClient).getOwnershipWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Ownership",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentInvitationApi(apiClient).queryInvitationWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Invitation",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentOwnershipApi(apiClient).queryOwnershipWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Ownership",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentInvitationApi(apiClient).updateInvitationWithHttpInfo(
                                        1L, 1L, updateApartmentInvitation());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Invitation",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new ApartmentOwnershipApi(apiClient).updateOwnershipWithHttpInfo(
                                        1L, 1L,
                                        new UpdateOwnership().ownershipPart(BigDecimal.valueOf(0.5)));
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Ownership",
                        false, true, false, false),

// NewsApiImpl
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new NewsApi(apiClient).createNewsWithHttpInfo(createNews());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create News",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new NewsApi(apiClient).deleteNewsWithHttpInfo(1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete News",
                        true, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new NewsApi(apiClient).getAllNewsWithHttpInfo(
                                        1, 1, "id,asc", null,
                                        null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get All News",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new NewsApi(apiClient).getNewsWithHttpInfo(1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get News",
                        true, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new NewsApi(apiClient).updateNewsWithHttpInfo(1L, updateNews());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update News",
                        true, true, false, false),

//PollApiImpl
                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PolledHouseApi(apiClient).createPolledHouseWithHttpInfo(1L,
                                        new HouseLookup().id(1L));
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create Polled House",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PollQuestionApi(apiClient).createQuestionWithHttpInfo(
                                        1L, createAdviceQuestion());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "create Question",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PolledHouseApi(apiClient).deletePolledHouseWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Polled House",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PollQuestionApi(apiClient).deleteQuestionWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "delete Question",
                        false, true, false, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PollApi(apiClient).getPollWithHttpInfo(1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Poll",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PolledHouseApi(apiClient).getPolledHouseWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Polled House",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PollQuestionApi(apiClient).getQuestionWithHttpInfo(
                                        1L, 1L);
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "get Question",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PollApi(apiClient).queryPollWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Poll",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PolledHouseApi(apiClient).queryPolledHouseWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null, null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Polled House",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PollQuestionApi(apiClient).queryQuestionWithHttpInfo(
                                        1L, 1, 10, "id,asc", null,
                                        null, null);

                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "query Question",
                        false, true, true, false),

                Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                            try {
                                return new PollQuestionApi(apiClient).updateQuestionWithHttpInfo(
                                        1L, 1L, updateQuestion());
                            } catch (ApiException e) {
                                return new ApiResponse<Void>(e.getCode(), null);
                            }
                        },
                        "update Question",
                        false, true, false, false)

        );
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
            if (statusCode == 401) {
                Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), statusCode);
            } else {
                Assertions.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), statusCode);
            }
        }
    }

    public void checkUnauthenticatedUser(boolean unauthenticatedPermission, int statusCode) {
        if (unauthenticatedPermission) {
            Assertions.assertNotEquals(Response.Status.UNAUTHORIZED.getStatusCode(), statusCode);
            Assertions.assertNotEquals(Response.Status.FORBIDDEN.getStatusCode(), statusCode);
        } else {
            if (statusCode == 401) {
                Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), statusCode);
            } else {
                Assertions.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), statusCode);
            }
        }
    }

    // Auxiliary methods
    private static UpdateQuestion updateQuestion() {
        return new UpdateQuestion()
                .question("Do you like updated question?")
                .type(QuestionType.ADVICE);
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

    private static CreateNews createNews() {
        return new CreateNews()
                .title("Title")
                .description("Description")
                .text("Text")
                .photoUrl("http://someurl.example.com")
                .source("Source");
    }

    private static UpdateApartment updateApartment() {
        return new UpdateApartment()
                .number("27")
                .area(BigDecimal.valueOf(32.1));
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

    private static CreateApartment createApartment() {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15")
                .invitations(createListOfInvitation());
    }

    private static UpdatePoll updatePoll() {
        LocalDateTime completionDate = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plusDays(2L)
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

    private static UpdateCooperation updateCooperation() {
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

    private static CreateContact createEmailContact() {
        return new CreateEmailContact()
                .email("newEmailContact@example.com")
                .main(false)
                .type(ContactType.EMAIL);
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
    private static ReadCooperation createAndReadCooperationForPoll() {
        return cooperationApi.createCooperationWithHttpInfo(createCooperationForPoll()).getData();
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
    private static CreatePoll createPoll(ReadCooperation readCooperation) {

        LocalDateTime completionDate = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plusDays(2L)
                .plusMinutes(1L);
        return new CreatePoll()
                .header("Poll for our houses")
                .type(PollType.SIMPLE)
                .completionDate(completionDate)
                .addHousesItem(new HouseLookup().id(readCooperation.getHouses().get(0).getId()))
                .addHousesItem(new HouseLookup().id(readCooperation.getHouses().get(1).getId()));
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
