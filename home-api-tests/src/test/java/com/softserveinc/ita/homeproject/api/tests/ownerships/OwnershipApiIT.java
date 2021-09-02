package com.softserveinc.ita.homeproject.api.tests.ownerships;

import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.ApartmentApi;
import com.softserveinc.ita.homeproject.client.api.ApartmentOwnershipApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.HouseApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.client.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
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

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OwnershipApiIT {

    private final ApartmentOwnershipApi ownershipApi = new ApartmentOwnershipApi(ApiClientUtil.getCooperationAdminClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getAdminClient());

    private final UserApi userApi = new UserApi(ApiClientUtil.getAdminClient());

    private static final long TEST_OWNERSHIP_ID = 10000001L;

    private static final long TEST_DELETE_OWNERSHIP_ID = 10000003L;

    private static final long TEST_APARTMENT_ID = 100000000L;

    private static final long TEST_DELETE_OWNERSHIP_APARTMENT_ID = 100000001L;

    @Test
    void createUserAndOwnershipViaApartmentTest() throws ApiException, InterruptedException, IOException {
        CreateApartment createApartment = createApartment();

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

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

        ApiResponse<ReadOwnership> response
                = ownershipApi.getOwnershipWithHttpInfo(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

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
                        .getOwnershipWithHttpInfo(wrongId, TEST_OWNERSHIP_ID))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + TEST_OWNERSHIP_ID + "' is not found");
    }

    @Test
    void updateOwnershipTest() throws ApiException {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

        ApiResponse<ReadOwnership> response =
                ownershipApi.updateOwnershipWithHttpInfo(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID, updateOwnership);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertOwnership(expectedOwnership, updateOwnership, response.getData());
    }

    @Test
    void updateOwnershipWithInvalidOwnershipPart() throws ApiException {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.8));

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

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
                        .updateOwnershipWithHttpInfo(wrongId, TEST_OWNERSHIP_ID, updateOwnership))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + TEST_OWNERSHIP_ID + "' is not found");
    }

    @Test
    void deleteOwnershipTest() throws ApiException {

        ApiResponse<Void> response = ownershipApi.deleteOwnershipWithHttpInfo(TEST_DELETE_OWNERSHIP_APARTMENT_ID, TEST_DELETE_OWNERSHIP_ID);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi.getOwnership(TEST_DELETE_OWNERSHIP_APARTMENT_ID, TEST_DELETE_OWNERSHIP_ID));

    }

    @Test
    void deleteOwnershipWithNonExistentApartment() {

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .deleteOwnershipWithHttpInfo(wrongId, TEST_OWNERSHIP_ID))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + TEST_OWNERSHIP_ID + "' is not found");
    }

    private CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"));
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name("newCooperationTest")
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
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

    private CreateApartment createApartment() {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15")
                .invitations(createApartmentInvitation());
    }

    private List<CreateInvitation> createApartmentInvitation() {
        List<CreateInvitation> createInvitations = new ArrayList<>();
        createInvitations.add(new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.3))
                .email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .type(InvitationType.APARTMENT));

        return createInvitations;
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
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }
}
