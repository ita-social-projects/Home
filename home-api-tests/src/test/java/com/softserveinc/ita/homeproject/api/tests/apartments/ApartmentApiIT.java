package com.softserveinc.ita.homeproject.api.tests.apartments;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.ApartmentApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.HouseApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.CreateApartment;
import com.softserveinc.ita.homeproject.client.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateCooperationAdminData;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreateInvitation;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.ReadApartment;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import com.softserveinc.ita.homeproject.client.model.UpdateApartment;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class ApartmentApiIT {

    private final static int NUMBER_OF_APARTMENT_INVITATIONS = 2;

    private final static Long APARTMENT_ID = 100L;

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getCooperationAdminClient());

    @Test
    void createApartmentTest() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());

        ApiResponse<ReadApartment> response = apartmentApi.createApartmentWithHttpInfo(createdHouse.getId(), createApartment);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertApartment(createApartment, response.getData());
    }

    @Test
    void createApartmentWithNonExistentHouse() {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .createApartmentWithHttpInfo(wrongId, createApartment))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("House with 'id: " + wrongId +"' is not found");
    }

    @Test
    void createApartmentWithExistingApartmentNumberTest() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());

        ApiResponse<ReadApartment> response = apartmentApi.createApartmentWithHttpInfo(createdHouse.getId(), createApartment);

        CreateApartment createExistingApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> apartmentApi
                .createApartmentWithHttpInfo(createdHouse.getId(), createExistingApartment))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Apartment with number " + createExistingApartment.getNumber()
                +" already exist in this house");
    }

    @Test
    void getApartmentTest() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

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
            .withMessageContaining("Apartment with 'id: " + wrongId + "' is not found");
    }

    @Test
    void getApartmentWithNonExistentHouse() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment expectedApartment = apartmentApi.createApartment(createdHouse.getId(), createApartment);

        Long wrongId = 10000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .getApartmentWithHttpInfo(wrongId, expectedApartment.getId()))
                .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Apartment with 'id: " + expectedApartment.getId() + "' is not found");
    }

    @Test
    void createApartmentInvalidApartmentNumber() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCooperation.getId(), createHouse());
        CreateApartment emptyNumber = createApartment(NUMBER_OF_APARTMENT_INVITATIONS).number("");
        CreateApartment longNumber = createApartment(NUMBER_OF_APARTMENT_INVITATIONS).number("1000000000-a");

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

    @Test
    void updateApartmentTest() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(),createApartment);

        UpdateApartment updateApartment = new UpdateApartment()
                .number("27")
                .area(BigDecimal.valueOf(32.1));

        ApiResponse<ReadApartment> response = apartmentApi.updateApartmentWithHttpInfo(createdHouse.getId(),createdApartment.getId(), updateApartment);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertApartment(createdApartment, updateApartment, response.getData());
    }

    @Test
    void updateNonExistentApartment() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());

        UpdateApartment updateApartment = new UpdateApartment()
                .number("27")
                .area(BigDecimal.valueOf(32.1));

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .updateApartmentWithHttpInfo(createdHouse.getId(), wrongId, updateApartment))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Apartment with 'id: " + wrongId +"' is not found");
    }

    @Test
    void updateApartmentWithNonExistentHouse() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(),createApartment);

        UpdateApartment updateApartment = new UpdateApartment()
                .number("27")
                .area(BigDecimal.valueOf(32.1));

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .updateApartmentWithHttpInfo(wrongId, createdApartment.getId(), updateApartment))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Apartment with 'id: " + createdApartment.getId() +"' is not found");
    }

    @Test
    void deleteApartmentTest() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(),createApartment);

        ApiResponse<Void> response = apartmentApi.deleteApartmentWithHttpInfo(createdHouse.getId(), createdApartment.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi.getApartment(createdHouse.getId(), createdApartment.getId()));
    }

    @Test
    void deleteApartmentWithNonExistentHouse() throws ApiException {
        CreateApartment createApartment = createApartment(NUMBER_OF_APARTMENT_INVITATIONS);

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(),createApartment);

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .deleteApartmentWithHttpInfo(wrongId, createdApartment.getId()))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Apartment with 'id: " + createdApartment.getId() +"' is not found");
    }

    @Test
    void deleteNonExistentApartment() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> apartmentApi
                        .deleteApartmentWithHttpInfo(createdHouse.getId(), wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Apartment with 'id: " + wrongId +"' is not found");
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomNumeric(8))
                .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
                .adminData(new CreateCooperationAdminData().email("test.receive.messages@gmail.com"))
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

    private CreateApartment createApartment(int numberOfApartmentInvitations) {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15");
    }

    private List<CreateInvitation> createApartmentInvitation(int numberOfInvitations) {
        return Stream.generate(CreateApartmentInvitation::new)
            .map(x -> x.apartmentId(APARTMENT_ID).email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .type(InvitationType.APARTMENT))
            .limit(numberOfInvitations)
            .collect(Collectors.toList());
    }


    private void assertApartment(CreateApartment expected, ReadApartment actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getArea(), actual.getApartmentArea());
        assertEquals(expected.getNumber(), actual.getApartmentNumber());
    }

    private void assertApartment(ReadApartment saved, UpdateApartment update, ReadApartment updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getNumber(), updated.getApartmentNumber());
        assertEquals(update.getArea(), updated.getApartmentArea());
    }
}
