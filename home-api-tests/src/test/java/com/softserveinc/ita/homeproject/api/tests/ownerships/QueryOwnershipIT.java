package com.softserveinc.ita.homeproject.api.tests.ownerships;

import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.MailUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.ResponseEmailItem;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.*;
import com.softserveinc.ita.homeproject.api.tests.query.OwnershipQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryOwnershipIT {

    private final static Long APARTMENT_ID = 100L;

    private final ApartmentOwnershipApi ownershipApi = new ApartmentOwnershipApi(ApiClientUtil.getCooperationAdminClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final UserApi userApi = new UserApi(ApiClientUtil.getCooperationAdminClient());


    private static long TEST_APARTMENT_ID = 1L;

    private static List<Long> ownershipsID = new ArrayList<>();


    @BeforeAll
    void createOwnershipsAndAppartament() throws ApiException, InterruptedException, IOException {
        int numInvitations = 3;
        CreateApartment createApartment = createApartment(numInvitations);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(), createApartment);

        TEST_APARTMENT_ID = createdApartment.getId();

        List<CreateUser> users = Stream.generate(CreateUser::new).map(x ->
                x.firstName("firstName")
                    .middleName("middleName")
                    .lastName("middleName")
                    .password("password")
                    .email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com")))
            .limit(numInvitations)
            .collect(Collectors.toList());

        users.forEach(x -> {
            String email = Objects.requireNonNull(
                    createApartment.getInvitations().get(createApartment.getInvitations().size() - 1)).getEmail();
            ResponseEmailItem letter = MailUtil.waitLetter(MailUtil.predicate().email(email).subject("apartment"));
            x.setRegistrationToken(MailUtil.getToken(letter));
            x.setEmail(email);
            try {
                ApiResponse<ReadUser> response = userApi.createUserWithHttpInfo(x);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            createApartment.getInvitations().remove(createApartment.getInvitations().size() - 1);
        });

        ownershipApi.queryOwnership(TEST_APARTMENT_ID, null, null, null, null, null, null, null)
            .forEach(x -> ownershipsID.add(x.getId()));
    }

    @Test
    void getAllOwnershipsAscSort() throws ApiException {

        List<ReadOwnership> queryResponse = new OwnershipQuery.Builder(ownershipApi)
                .apartmentId(TEST_APARTMENT_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(ReadOwnership::getId));
    }

    @Test
    void getAllOwnershipsDescSort() throws ApiException {
        List<ReadOwnership> queryResponse = new OwnershipQuery.Builder(ownershipApi)
                .apartmentId(TEST_APARTMENT_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(ReadOwnership::getId).reversed());
    }

    @Test
    void getAllOwnershipsFromNotExistingApartment() {
        Long wrongApartmentId = 999999999L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> new OwnershipQuery
                        .Builder(ownershipApi)
                        .apartmentId(wrongApartmentId)
                        .pageNumber(1)
                        .pageSize(10)
                        .build().perform())
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Apartment with 'id: " + wrongApartmentId + "' is not found");
    }

    @Test
    void getAllOwnershipsFilteredByOwnershipPart() throws ApiException {

        List<ReadOwnership> queryResponse = new OwnershipQuery.Builder(ownershipApi)
                .apartmentId(TEST_APARTMENT_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .filter("ownershipPart=bt=(0.2,0.6)")
                .build().perform();

        queryResponse
                .forEach(element -> assertTrue(Objects.requireNonNull(element.getOwnershipPart())
                        .compareTo(valueOf(0.2)) > 0 && element.getOwnershipPart().compareTo(valueOf(0.6)) < 0));
        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(ReadOwnership::getId));
    }

    @Test
    void getAllOwnershipsByOwnershipId() throws ApiException {
        List<ReadOwnership> queryResponse = new OwnershipQuery.Builder(ownershipApi)
                .apartmentId(TEST_APARTMENT_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .id(ownershipsID.get(0))
                .build().perform();
        queryResponse.forEach(element -> assertEquals(ownershipsID.get(0), element.getId()));
    }

    @Test
    void getAllOwnershipsByOwnershipPart() throws ApiException {
        UpdateOwnership updateOwnership2 = new UpdateOwnership()
                .ownershipPart(valueOf(0.4));
        ownershipApi.updateOwnership(TEST_APARTMENT_ID,ownershipsID.get(0), updateOwnership2);
        ReadOwnership readOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, ownershipsID.get(0));
        String ownershipPart = readOwnership.getOwnershipPart();

        List<ReadOwnership> queryResponse = new OwnershipQuery.Builder(ownershipApi)
                .apartmentId(TEST_APARTMENT_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .ownershipPart(ownershipPart)
                .build().perform();

        queryResponse
                .forEach(element -> assertEquals(element.getOwnershipPart(), readOwnership.getOwnershipPart()));
    }

    @Test
    void getAllOwnershipsByUserId() throws ApiException {
        ReadOwnership readOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, ownershipsID.get(0));

        Long userId = Objects.requireNonNull(readOwnership.getOwner()).getId();

        List<ReadOwnership> queryResponse = new OwnershipQuery.Builder(ownershipApi)
                .apartmentId(TEST_APARTMENT_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .userId(userId)
                .build().perform();

        queryResponse
                .forEach(element -> assertEquals(element.getOwner(), readOwnership.getOwner()));
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
        return Stream.generate(CreateApartmentInvitation::new)
            .map(x -> x.apartmentId(APARTMENT_ID).email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .type(InvitationType.APARTMENT))
            .limit(numberOfInvitations)
            .collect(Collectors.toList());
    }
}
