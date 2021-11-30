package com.softserveinc.ita.homeproject.api.tests.security;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.ContactApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateAdviceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateApartment;
import com.softserveinc.ita.homeproject.client.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreateInvitation;
import com.softserveinc.ita.homeproject.client.model.CreateNews;
import com.softserveinc.ita.homeproject.client.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.client.model.CreatePoll;
import com.softserveinc.ita.homeproject.client.model.CreateQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateUser;
import com.softserveinc.ita.homeproject.client.model.CreateVote;
import com.softserveinc.ita.homeproject.client.model.HouseLookup;
import com.softserveinc.ita.homeproject.client.model.InvitationToken;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.PollStatus;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.QuestionType;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadVote;
import com.softserveinc.ita.homeproject.client.model.UpdateApartment;
import com.softserveinc.ita.homeproject.client.model.UpdateApartmentInvitation;
import com.softserveinc.ita.homeproject.client.model.UpdateContact;
import com.softserveinc.ita.homeproject.client.model.UpdateCooperation;
import com.softserveinc.ita.homeproject.client.model.UpdateEmailContact;
import com.softserveinc.ita.homeproject.client.model.UpdateHouse;
import com.softserveinc.ita.homeproject.client.model.UpdateNews;
import com.softserveinc.ita.homeproject.client.model.UpdateOwnership;
import com.softserveinc.ita.homeproject.client.model.UpdatePoll;
import com.softserveinc.ita.homeproject.client.model.UpdateQuestion;
import com.softserveinc.ita.homeproject.client.model.UpdateUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PermissionsIT {

    private final static CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    static private final List<ApiClientMethods> apiClientMethods =
        getAllApiClientMethods(listApiClientClassInstances());

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testCooperationAdmin(Function<ApiClient, ApiResponse<?>> action, String x,
                              boolean admin, boolean coopAdmin) {

        int statusCode = getStatusCode(action, ApiClientUtil.getCooperationAdminClient());
        checkUser(coopAdmin, statusCode);
    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testOwner(Function<ApiClient, ApiResponse<?>> action, String x,
                   boolean admin, boolean coopAdmin, boolean owner) {
        int statusCode = getStatusCode(action, ApiClientUtil.getOwnerClient());
        checkUser(owner, statusCode);
    }

    @ParameterizedTest(name = "{index}-{1}")
    @MethodSource("check")
    void testGuestUser(Function<ApiClient, ApiResponse<?>> action, String x,
                       boolean admin, boolean coopAdmin, boolean owner, boolean guestUser) {

        int statusCode = getStatusCode(action, ApiClientUtil.getUserGuestClient());
        checkUser(guestUser, statusCode);
    }

    static private Stream<Arguments> check() {
        Arguments[] values = new Arguments[apiClientMethods.size()];
        int i = 0;
        for (ApiClientMethods acm : apiClientMethods) {

            values[i++] = Arguments.of((Function<ApiClient, ApiResponse<?>>) (ApiClient apiClient) -> {
                    int count = 0;
                    int param = 0;
                    int stab = 0;
                    try {
                        if (acm.getMethod().getParameterCount() == count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName())
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient));
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param])
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab]));
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );

                        } else if (acm.getMethod().getParameterCount() == ++count) {
                            return (ApiResponse<?>) acm.getApi().getClass().getMethod(acm.getMethodName(),
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param++],
                                    acm.getMethod().getParameterTypes()[param]
                                )
                                .invoke(acm.getApi().getClass().getConstructor(ApiClient.class).newInstance(apiClient),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab++]),
                                    getStubParam(acm.getMethod().getParameterTypes()[stab])
                                );
                        }

                    } catch (InvocationTargetException | IllegalAccessException |
                        InstantiationException | NoSuchMethodException e) {
                        String regex = ":\\d{3},";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(e.getCause().getMessage());
                        StringBuilder s = new StringBuilder();
                        if (matcher.find()) {
                            s.append(matcher.group(0)
                                .replaceAll(":", "")
                                .replaceAll(",", ""));
                        }
                        return new ApiResponse<Void>(Integer.parseInt(s.toString()), null);
                    }
                    throw new UnsupportedOperationException();
                },
                acm.getMethodName().replaceAll("\\QWithHttpInfo\\E", ""),
                acm.getAdmin(),
                acm.getCoopAdmin(),
                acm.getOwner(),
                acm.getUserGuest()
            );
        }

        return Arrays.stream(values);
    }

    private static Object getStubParam(Class<?> parameterType) {
        switch (parameterType.getName()) {
            case "java.lang.String":
            case "com.softserveinc.ita.homeproject.client.model.ContactType":
            case "com.softserveinc.ita.homeproject.client.model.PollStatus":
            case "com.softserveinc.ita.homeproject.client.model.QuestionType":
            case "com.softserveinc.ita.homeproject.client.model.PollType":
            case "java.time.LocalDateTime":
                return null;
            case "java.lang.Integer":
                return 1;
            case "java.lang.Long":
                return 1L;
            case "java.math.BigDecimal":
                return new BigDecimal(1);
            case "com.softserveinc.ita.homeproject.client.model.UpdateUser":
                return new UpdateUser();
            case "com.softserveinc.ita.homeproject.client.model.CreateUser":
                return new CreateUser();
            case "com.softserveinc.ita.homeproject.client.model.HouseLookup":
                return new HouseLookup().id(1L);
            case "com.softserveinc.ita.homeproject.client.model.UpdateOwnership":
                return new UpdateOwnership().ownershipPart(BigDecimal.valueOf(0.5));
            case "com.softserveinc.ita.homeproject.client.model.CreatePoll":
                return createPoll(createAndReadCooperationForPoll());
            case "com.softserveinc.ita.homeproject.client.model.UpdatePoll":
                return updatePoll();
            case "com.softserveinc.ita.homeproject.client.model.CreateVote":
                return createVote();
            case "com.softserveinc.ita.homeproject.client.model.ReadVote":
                return readVote();
            case "com.softserveinc.ita.homeproject.client.model.createAdviceQuestion":
            case "com.softserveinc.ita.homeproject.client.model.CreateQuestion":
                return createAdviceQuestion();
            case "com.softserveinc.ita.homeproject.client.model.CreateContact":
                return createEmailContact();
            case "com.softserveinc.ita.homeproject.client.model.UpdateContact":
                return updateEmailContact();
            case "com.softserveinc.ita.homeproject.client.model.CreateHouse":
                return createHouse();
            case "com.softserveinc.ita.homeproject.client.model.UpdateHouse":
                return updateHouse();
            case "com.softserveinc.ita.homeproject.client.model.CreateApartment":
                return createApartment();
            case "com.softserveinc.ita.homeproject.client.model.UpdateApartment":
                return updateApartment();
            case "com.softserveinc.ita.homeproject.client.model.UpdateQuestion":
                return updateQuestion();
            case "com.softserveinc.ita.homeproject.client.model.CreateNews":
                return createNews();
            case "com.softserveinc.ita.homeproject.client.model.UpdateNews":
                return updateNews();
            case "com.softserveinc.ita.homeproject.client.model.UpdateCooperation":
                return updateCooperation();
            case "com.softserveinc.ita.homeproject.client.model.CreateCooperation":
                return createBaseCooperation();
            case "com.softserveinc.ita.homeproject.client.model.CreateApartmentInvitation":
                return createApartmentInvitation();
            case "com.softserveinc.ita.homeproject.client.model.UpdateApartmentInvitation":
                return updateApartmentInvitation();
            case "com.softserveinc.ita.homeproject.client.model.InvitationToken":
                return getInvitationToken();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private int getStatusCode(Function<ApiClient, ApiResponse<?>> action, ApiClient unauthorizedClient) {
        int statusCode;
        ApiResponse<?> resp = action.apply(unauthorizedClient);
        statusCode = resp.getStatusCode();
        return statusCode;
    }

    public void checkUser(boolean role, int statusCode) {
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ApiClientMethods {
        private Object api;

        private Method method;

        private String methodName;

        private Boolean admin;

        private Boolean coopAdmin;

        private Boolean owner;

        private Boolean userGuest;
    }

    // populate of class instance via inner class
    private static List<ApiClientMethods> getAllApiClientMethods(List<Object> clientApiInstances) {
        ArrayList<ApiClientMethods> allMethods = new ArrayList<>();
        for (Object instance : clientApiInstances) {
            getApiMethods(instance);
            for (Method aMethod : getApiMethods(instance)) {
                String methodName = aMethod.toString()
                    .split("\\(")[0]
                    .split("\\.")[aMethod.toString().split("\\(")[0].split("\\.").length - 1];
                boolean[] permission = findPermission(methodName.replaceAll("\\QWithHttpInfo\\E", ""));
                allMethods.add(new ApiClientMethods(instance, aMethod, methodName,
                    permission[0], permission[1], permission[2], permission[3]));
            }
        }
        return allMethods;
    }

    private static boolean[] findPermission(String methodName) {
        boolean[] setPermission;
        switch (methodName) {
            case "createUser":
            case "createCooperation":
                setPermission = new boolean[]{true, true, true, true};
                break;
            case "createContactOnUser":
            case "getUser":
            case "queryContactsOnUser":
            case "deleteContactOnUser":
            case "getNews":
            case "getAllNews":
            case "queryContactsOnCooperation":
            case "queryHouse":
            case "queryCooperation":
            case "getContactOnCooperation":
            case "getHouse":
            case "getCooperation":
            case "updateUser":
            case "updateContactOnUser":
            case "deleteUser":
            case "getAllUsers":
            case "getContactOnUser":
            case "getCurrentUser":
                setPermission = new boolean[]{true, true, true, false};
                break;
            case "createHouse":
            case "updateNews":
            case "deleteNews":
            case "createNews":
            case "updateContactOnCooperation":
            case "updateHouse":
            case "updateCooperation":
            case "deleteContactOnCooperation":
            case "deleteHouse":
            case "createContactOnCooperation":
                setPermission = new boolean[]{true, true, false, false};
                break;
            case "deleteCooperation":
                setPermission = new boolean[]{true, false, false, false};
                break;
            case "getCooperationPoll":
            case "queryQuestion":
            case "queryPolledHouse":
            case "queryPoll":
            case "getQuestion":
            case "getPolledHouse":
            case "getPoll":
            case "queryOwnership":
            case "getOwnership":
            case "queryApartment":
            case "getApartment":
            case "queryCooperationPoll":
                setPermission = new boolean[]{false, true, true, false};
                break;
            case "approveInvitation":
            case "updateQuestion":
            case "deleteQuestion":
            case "deletePolledHouse":
            case "createQuestion":
            case "createPolledHouse":
            case "updateOwnership":
            case "updateInvitation":
            case "queryInvitation":
            case "getInvitation":
            case "deleteOwnership":
            case "deleteInvitation":
            case "createInvitation":
            case "updateApartment":
            case "deleteApartment":
            case "createApartment":
            case "updateCooperationPoll":
            case "deleteCooperationPoll":
            case "createCooperationPoll":
            case "readVote":
                setPermission = new boolean[]{false, true, false, false};
                break;
            case "createVote":
                setPermission = new boolean[]{false, false, true, false};
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return setPermission;
    }

    @SneakyThrows
    private static List<Object> listApiClientClassInstances() {
        List<Object> classInstances = new ArrayList<>();
        for (Class<?> aClass : getAllClassesOfClientApi()) {
            classInstances.add(aClass.getConstructor().newInstance());
        }
        return classInstances;
    }

    @SneakyThrows
    private static List<Class<?>> getAllClassesOfClientApi() {
        return getAllClassesFromPackage(ContactApi.class.getPackageName());
    }

    private static List<Class<?>> getAllClassesFromPackage(String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
            .getAllTypes()
            .stream()
            .map(name -> {
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private static ArrayList<Method> getApiMethods(Object someApiInstance) {
        return Arrays.stream(someApiInstance.getClass().getMethods())
            .filter(s -> s.toString().contains("WithHttpInfo"))
            .collect(Collectors.toCollection(ArrayList::new));
    }


    // Auxiliary methods
    private static InvitationToken getInvitationToken() {
        InvitationToken invitationToken = new InvitationToken();
        invitationToken.setInvitationToken("95eb8223-f2d4-11eb-82f4-2f106ba224d5");
        return invitationToken;
    }

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
            .email("test.receive.messages@gmail.com")
            .type(InvitationType.APARTMENT));

        createInvitations.add(new CreateApartmentInvitation()
            .email("test.receive.messages@gmail.com")
            .type(InvitationType.APARTMENT));

        return createInvitations;
    }

    private static CreateApartmentInvitation createApartmentInvitation() {
        return (CreateApartmentInvitation) new CreateApartmentInvitation()
            .email("test.receive.messages@gmail.com")
            .type(InvitationType.APARTMENT);
    }

    private static UpdateApartmentInvitation updateApartmentInvitation() {
        return new UpdateApartmentInvitation()
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
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
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
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminEmail(RandomStringUtils.randomAlphabetic(12).concat("@gmail.com"))
            .address(createAddress())
            .contacts(createContactList());
    }

    private static CreateCooperation createCooperationForPoll() {
        return new CreateCooperation()
            .name("newCooperationTest")
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
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
            .addHousesItem(new HouseLookup().id(Objects.requireNonNull(readCooperation.getHouses()).get(0).getId()))
            .addHousesItem(new HouseLookup().id(readCooperation.getHouses().get(1).getId()));
    }

    @SneakyThrows
    private static CreateVote createVote() {
        return new CreateVote();
    }

    @SneakyThrows
    private static ReadVote readVote() {
        return new ReadVote();
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
