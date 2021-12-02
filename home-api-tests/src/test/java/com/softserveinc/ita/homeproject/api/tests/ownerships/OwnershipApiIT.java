package com.softserveinc.ita.homeproject.api.tests.ownerships;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.ApartmentApi;
import com.softserveinc.ita.homeproject.client.api.ApartmentOwnershipApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.HouseApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.client.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OwnershipApiIT {

    private final ApartmentOwnershipApi ownershipApi = new ApartmentOwnershipApi(ApiClientUtil.getCooperationAdminClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final UserApi userApi = new UserApi(ApiClientUtil.getCooperationAdminClient());

    private static List<Long> ownershipsID = new ArrayList<>();

    private static long TEST_APARTMENT_ID = 3L;

    private static long TEST_DELETE_OWNERSHIP_APARTMENT_ID = 2L;

    @BeforeAll
    void createOwnershipsAndAppartament() throws ApiException, InterruptedException, IOException {
        CreateApartment createApartment = createApartment(3);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(), createApartment);

        TEST_APARTMENT_ID = createdApartment.getId();
        TEST_DELETE_OWNERSHIP_APARTMENT_ID = createdApartment.getId();

        TimeUnit.MILLISECONDS.sleep(5000);

        int num =  createApartment.getInvitations().size();
        List <CreateUser> users = new ArrayList<>();
        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);
        for (int i = 0; i < num; i++) {
            users.add(createBaseUser());
            users.get(i).setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse,
                    Objects.requireNonNull(createApartment.getInvitations().get(i)).getEmail())));
            users.get(i).setEmail(Objects.requireNonNull(createApartment.getInvitations().get(i).getEmail()));
            ApiResponse<ReadUser> response = userApi.createUserWithHttpInfo(users.get(i));
        }

        List<ReadOwnership> ownershipList = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            ownershipList.add(ownershipApi.queryOwnership(TEST_APARTMENT_ID, null, null, null, null, null, null, null).get(i));
            ownershipsID.add(ownershipList.get(i).getId());
        }
    }

    @Test
    void createUserAndOwnershipViaApartmentTest() throws ApiException, InterruptedException, IOException {
        CreateApartment createApartment = createApartment(1);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        apartmentApi.createApartment(createdHouse.getId(), createApartment);

        TimeUnit.MILLISECONDS.sleep(5000);
        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);
        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse,
                Objects.requireNonNull(createApartment.getInvitations()).get(0).getEmail())));

        expectedUser.setEmail(Objects.requireNonNull(createApartment.getInvitations()).get(0).getEmail());

        ApiResponse<ReadUser> response = userApi.createUserWithHttpInfo(expectedUser);

        assertEquals(Response.Status.CREATED.getStatusCode(),
                response.getStatusCode());
        assertUser(expectedUser, response.getData());
    }

    @Test
    void getOwnershipTest() throws ApiException {

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, ownershipsID.get(0));

        ApiResponse<ReadOwnership> response
                = ownershipApi.getOwnershipWithHttpInfo(TEST_APARTMENT_ID, ownershipsID.get(0));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertApartment(expectedOwnership, response.getData());
    }

    @Test
    void getNonExistentOwnership() {
        Long wrongId = 20000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .getOwnershipWithHttpInfo(TEST_APARTMENT_ID, wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + wrongId + "' is not found");
    }

    @Test
    void getOwnershipWithNonExistentApartment() {
        Long wrongId = 20000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .getOwnershipWithHttpInfo(wrongId, ownershipsID.get(0)))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + ownershipsID.get(0) + "' is not found");
    }

    @Test
    void updateOwnershipTest() throws ApiException {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, ownershipsID.get(0));

        ApiResponse<ReadOwnership> response =
                ownershipApi.updateOwnershipWithHttpInfo(TEST_APARTMENT_ID, ownershipsID.get(0), updateOwnership);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertOwnership(expectedOwnership, updateOwnership, response.getData());
    }

    @Test
    void updateOwnershipWithInvalidOwnershipPart() throws ApiException {
        UpdateOwnership updateOwnership2 = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.4));
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.8));
        ownershipApi.updateOwnership(TEST_APARTMENT_ID,ownershipsID.get(1), updateOwnership2);

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, ownershipsID.get(0));

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi.updateOwnership(TEST_APARTMENT_ID, expectedOwnership.getId(), updateOwnership))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("Entered sum of ownerships parts = 1.2 The sum of the entered ownership parts cannot be greater than 1");
    }

    @Test
    void updateNonExistentOwnershipTest() {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .updateOwnershipWithHttpInfo(TEST_APARTMENT_ID, wrongId, updateOwnership))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + wrongId + "' is not found");
    }

    @Test
    void updateOwnershipWithNonExistentApartmentTest() {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .updateOwnershipWithHttpInfo(wrongId, ownershipsID.get(0), updateOwnership))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + ownershipsID.get(0) + "' is not found");
    }

    @Test
    void deleteOwnershipTest() throws ApiException {

        ApiResponse<Void> response = ownershipApi.deleteOwnershipWithHttpInfo(TEST_DELETE_OWNERSHIP_APARTMENT_ID, ownershipsID.get(2));

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi.getOwnership(TEST_DELETE_OWNERSHIP_APARTMENT_ID, ownershipsID.get(2)));

    }

    @Test
    void deleteOwnershipWithNonExistentApartment() {

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .deleteOwnershipWithHttpInfo(wrongId, ownershipsID.get(0)))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + ownershipsID.get(0) + "' is not found");
    }

    private CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .password("password")
                .email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"));
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name("newCooperationTest")
                .usreo(RandomStringUtils.randomNumeric(8))
                .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
                .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .address(createAddress());
    }

    private CreateHouse createHouse() {
        return new CreateHouse()
                .adjoiningArea(500)
                .houseArea(BigDecimal.valueOf(500.0))
                .quantityFlat(50)
                .address(createAddress());
    }

    private Address createAddress() {
        return new Address().city("Dnepr")
                .district("District")
                .houseBlock("block")
                .houseNumber("number")
                .region("Dnipro")
                .street("street")
                .zipCode("zipCode");
    }

    private CreateApartment createApartment(int numberOfApartamentInvitations) {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15")
                .invitations(createApartmentInvitation(numberOfApartamentInvitations));
    }

    private List<CreateInvitation> createApartmentInvitation(int numberOfInvitations) {
        return  Stream.generate(CreateInvitation::new)
                .map(x -> x.email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com")).type(InvitationType.APARTMENT))
                .limit(numberOfInvitations)
                .collect(Collectors.toList());
    }

    private String getDecodedMessageByEmail(MailHogApiResponse response, String email) {
        String message = "";
        for (int i = 0; i < response.getItems().size(); i++) {
            if (response.getItems().get(i).getContent().getHeaders().getTo().contains(email)) {
                message = response.getItems().get(i).getMime().getParts().get(0).getMime().getParts().get(0).getBody();
                break;
            }
        }
        return new String(Base64.getMimeDecoder().decode(message), StandardCharsets.UTF_8);
    }

    private String getToken(String str) {
        Pattern pattern = Pattern.compile("(?<=:) .* (?=<)");
        Matcher matcher = pattern.matcher(str);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }

        return result.trim();
    }

    private void assertApartment(ReadOwnership expected, ReadOwnership actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getOwnershipPart(), actual.getOwnershipPart());
    }

    private void assertOwnership(ReadOwnership saved, UpdateOwnership update, ReadOwnership updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getOwnershipPart(), updated.getOwnershipPart());
    }

    private void assertUser(CreateUser expected, ReadUser actual) {
        assertNotNull(expected);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }
}
