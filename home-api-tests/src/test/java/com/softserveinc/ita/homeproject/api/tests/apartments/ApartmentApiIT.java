package com.softserveinc.ita.homeproject.api.tests.apartments;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.ApartmentApi;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.HouseApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.*;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApartmentApiIT {

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());


    @Test
    void createApartmentTest() throws ApiException {
        CreateApartment createApartment = createApartment();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());

        ApiResponse<ReadApartment> response = apartmentApi.createApartmentWithHttpInfo(createdHouse.getId(), createApartment);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertApartment(createApartment, response.getData());
    }

    @Test
    void createApartmentWithNonExistentHouse() throws ApiException{
        CreateApartment createApartment = createApartment();

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .createApartmentWithHttpInfo(wrongId, createApartment))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Can't find house with given ID: " + wrongId);
    }

    @Test
    void getApartmentTest() throws ApiException {
        CreateApartment createApartment = createApartment();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse expectedHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment expectedApartment = apartmentApi.createApartment(expectedHouse.getId(), createApartment);

        ApiResponse<ReadApartment> response = apartmentApi.getApartmentWithHttpInfo(expectedHouse.getId(), expectedApartment.getId());

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertApartment(createApartment, response.getData());
    }

    @Test
    void getNonExistentApartment() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse expectedHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());

        Long wrongId = 10000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .getApartmentWithHttpInfo(expectedHouse.getId(), wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Can't find apartment with given ID: " + wrongId);
    }

    @Test
    void getApartmentWithNonExistentHouse() throws ApiException {
        CreateApartment createApartment = createApartment();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment expectedApartment = apartmentApi.createApartment(createdHouse.getId(), createApartment);

        Long wrongId = 10000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .getApartmentWithHttpInfo(wrongId, expectedApartment.getId()))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Can't find house with given ID: " + wrongId);
    }

    @Test
    void createApartmentWithInvalidAreaOwner() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCooperation.getId(), createHouse());
        CreateApartment createApartment = createApartment().invitations(createInvalidApartmentInvitation());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi.createApartment(readHouse.getId(), createApartment))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("Area = 1.5. Area cannot be more than 1");
    }

    @Test
    void createApartmentInvalidApartmentNumber() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCooperation.getId(), createHouse());
        CreateApartment emptyNumber = createApartment().number("");
        CreateApartment longNumber = createApartment().number("1000000000-a");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi.createApartment(readHouse.getId(), emptyNumber))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `number` is invalid - must meet the rule.")
                .withMessageContaining("Parameter `number` is invalid - size must be between 1 and 6 signs.");
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi.createApartment(readHouse.getId(), longNumber))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `number` is invalid - must meet the rule.")
                .withMessageContaining("Parameter `number` is invalid - size must be between 1 and 6 signs.");
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name("newCooperationTest")
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
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

    private List<CreateApartmentInvitation> createApartmentInvitation() {
        List<CreateApartmentInvitation> createInvitations = new ArrayList<>();
        createInvitations.add((CreateApartmentInvitation) new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.3))
                .email("invitation@gmail.com")
                .type(InvitationType.APARTMENT));

        createInvitations.add((CreateApartmentInvitation) new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.7))
                .email("invitation2@gmail.com")
                .type(InvitationType.APARTMENT));

        return createInvitations;
    }

    private List<CreateApartmentInvitation> createInvalidApartmentInvitation() {
        List<CreateApartmentInvitation> createInvitations = new ArrayList<>();
        createInvitations.add((CreateApartmentInvitation) new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.8))
                .email("invitation@gmail.com")
                .type(InvitationType.APARTMENT));

        createInvitations.add((CreateApartmentInvitation) new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.7))
                .email("invitation2@gmail.com")
                .type(InvitationType.APARTMENT));

        return createInvitations;
    }

    private void assertApartment(CreateApartment expected, ReadApartment actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getArea(), actual.getApartmentArea());
        assertEquals(expected.getNumber(), actual.getApartmentNumber());
    }
}
