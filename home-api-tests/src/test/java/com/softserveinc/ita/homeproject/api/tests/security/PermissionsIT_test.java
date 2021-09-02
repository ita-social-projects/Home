package com.softserveinc.ita.homeproject.api.tests.security;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiClient;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.ContactApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.model.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.ws.rs.core.Response;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class PermissionsIT_test {

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
    @SneakyThrows
    @Test
    void reflectionConstructor(){
//        Class<?> contactApi = ContactApi.class;
//        Constructor[] constructors = contactApi.getConstructors();
        Constructor<?> constructor = ContactApi.class.getConstructor(ApiClient.class);
        ContactApi myObject = (ContactApi)
                constructor.newInstance(new ApiClient());
        System.out.println(myObject);
    }



    static Stream<Arguments> check() {


        return Stream.of(
/*        for (Object o :) {

        }*/
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
                        true, true, true, false)



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
