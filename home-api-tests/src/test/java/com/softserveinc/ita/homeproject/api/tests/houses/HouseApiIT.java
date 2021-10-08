package com.softserveinc.ita.homeproject.api.tests.houses;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.HouseApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import com.softserveinc.ita.homeproject.client.model.UpdateHouse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class HouseApiIT {

    private final Long wrongCooperationId = 999999999L;

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private static final String HOUSE_NOT_FOUND = "House with 'id: %d and cooperation_id %d' is not found";

    @Test
    void createHouseTest() throws ApiException {
        CreateHouse createHouse = createHouse();
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());

        ApiResponse<ReadHouse> response = houseApi.createHouseWithHttpInfo(createdCooperation.getId(), createHouse);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertHouse(createHouse, response.getData());
    }

    @Test
    void getHouseTest() throws ApiException {
        CreateHouse createHouse = createHouse();
        ReadCooperation expectedCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse expectedHouse = houseApi.createHouse(expectedCoop.getId(), createHouse);

        ApiResponse<ReadHouse> response = houseApi.getHouseWithHttpInfo(expectedCoop.getId(), expectedHouse.getId());

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertHouse(createHouse, response.getData());
    }

    @Test
    void updateHouseTest() throws ApiException {
        CreateHouse createHouse = createHouse();
        ReadCooperation expectedCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse expectedHouse = houseApi.createHouse(expectedCoop.getId(), createHouse);

        UpdateHouse updateHouse = getUpdatedHouse();

        ApiResponse<ReadHouse> response =
            houseApi.updateHouseWithHttpInfo(expectedCoop.getId(), expectedHouse.getId(), updateHouse);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertHouse(expectedHouse, updateHouse, response.getData());
    }

    @Disabled("Should be exception. Created task#284.")
    @Test
    void updateHouseFromNotExistingCooperationTest() throws ApiException {
        CreateHouse createHouse = createHouse();
        ReadCooperation expectedCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse expectedHouse = houseApi.createHouse(expectedCoop.getId(), createHouse);

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> houseApi.updateHouseWithHttpInfo(wrongCooperationId, expectedHouse.getId(), getUpdatedHouse()))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining(String.format(HOUSE_NOT_FOUND, expectedHouse.getId(), expectedCoop.getId()));
    }

    @Disabled("Should be exception. Created task#284.")
    @Test
    void updateHouseFromNotRelatedCooperationTest() throws ApiException {
        CreateHouse createHouse = createHouse();
        ReadCooperation expectedCoop = cooperationApi.createCooperation(createCooperation());
        ReadCooperation wrongCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse expectedHouse = houseApi.createHouse(expectedCoop.getId(), createHouse);

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> houseApi.updateHouseWithHttpInfo(wrongCoop.getId(), expectedHouse.getId(), getUpdatedHouse()))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining(String.format(HOUSE_NOT_FOUND, expectedHouse.getId(), expectedCoop.getId()));
    }

    @Test
    void deleteHouseTest() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCooperation.getId(), createHouse());

        ApiResponse<Void> response = houseApi.deleteHouseWithHttpInfo(readCooperation.getId(), readHouse.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.getHouse(readCooperation.getId(), readHouse.getId()));
    }

    @Test
    void createInvalidRegionAddressTest() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        CreateHouse emptyRegionHouse = createHouse().address(createAddress().region(""));
        CreateHouse longRegionHouse = createHouse().address(createAddress()
            .region("Address over 50 symbols Address over 50 symbols Address over 50 symbols."));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), emptyRegionHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `region` is invalid - size must be between 1 and 50 signs.");
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), longRegionHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `region` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createInvalidCityAddressTest() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        CreateHouse emptyCityHouse = createHouse().address(createAddress().city(""));
        CreateHouse longCityHouse = createHouse().address(createAddress()
            .city("Address over 50 symbols Address over 50 symbols Address over 50 symbols."));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), emptyCityHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `city` is invalid - size must be between 1 and 50 signs.");
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), longCityHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `city` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createInvalidDistrictAddressTest() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        CreateHouse emptyDistrictHouse = createHouse().address(createAddress().district(""));
        CreateHouse longDistrictHouse = createHouse().address(createAddress()
            .district("Address over 50 symbols Address over 50 symbols Address over 50 symbols."));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), emptyDistrictHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `district` is invalid - size must be between 1 and 50 signs.");
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), longDistrictHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `district` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createInvalidStreetAddressTest() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        CreateHouse emptyStreetHouse = createHouse();
        CreateHouse longStreetHouse = createHouse();
        emptyStreetHouse.setAddress(createAddress().street(""));
        longStreetHouse.setAddress(createAddress()
            .street("Address over 25 symbols Address over 25 symbols."));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), emptyStreetHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `street` is invalid - size must be between 1 and 25 signs.");
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), longStreetHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `street` is invalid - size must be between 1 and 25 signs.");
    }

    @Test
    void createInvalidHouseBlockAddressTest() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        CreateHouse emptyHouseBlockHouse = createHouse();
        CreateHouse longHouseBlockHouse = createHouse();
        emptyHouseBlockHouse.setAddress(createAddress().houseBlock(""));
        longHouseBlockHouse.setAddress(createAddress()
            .houseBlock("Address over 10 symbols."));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), emptyHouseBlockHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `houseBlock` is invalid - size must be between 1 and 10 signs.");
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), longHouseBlockHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `houseBlock` is invalid - size must be between 1 and 10 signs.");
    }

    @Test
    void createInvalidHouseNumberAddressTest() throws ApiException {
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCooperation());
        CreateHouse emptyHouseNumberHouse = createHouse();
        CreateHouse longHouseNumberHouse = createHouse();
        emptyHouseNumberHouse.setAddress(createAddress().houseNumber(""));
        longHouseNumberHouse.setAddress(createAddress()
            .houseNumber("Address over 10 symbols."));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), emptyHouseNumberHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `houseNumber` is invalid - size must be between 1 and 10 signs.");
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> houseApi.createHouse(readCooperation.getId(), longHouseNumberHouse))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `houseNumber` is invalid - size must be between 1 and 10 signs.");
    }

    private void assertHouse(CreateHouse expected, ReadHouse actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getAdjoiningArea(), actual.getAdjoiningArea());
        assertEquals(expected.getHouseArea(), actual.getHouseArea());
        assertEquals(expected.getQuantityFlat(), actual.getQuantityFlat());
    }

    private void assertHouse(ReadHouse saved, UpdateHouse update, ReadHouse updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getQuantityFlat(), updated.getQuantityFlat());
        assertEquals(update.getHouseArea(), updated.getHouseArea());
        assertEquals(update.getAdjoiningArea(), updated.getAdjoiningArea());
        assertEquals(update.getAddress(), updated.getAddress());
    }

    private CreateHouse createHouse() {
        return new CreateHouse()
            .adjoiningArea(500)
            .houseArea(BigDecimal.valueOf(500.0))
            .quantityFlat(50)
            .address(createAddress());
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
            .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminEmail("test.receive.messages@gmail.com")
            .address(createAddress())
            .houses(createHouseList())
            .contacts(createContactList());
    }

    private UpdateHouse getUpdatedHouse(){
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

    private List<CreateHouse> createHouseList() {
        List<CreateHouse> createHouses = new ArrayList<>();
        createHouses.add(new CreateHouse()
            .quantityFlat(96)
            .houseArea(BigDecimal.valueOf(4348.8))
            .adjoiningArea(400)
            .address(createAddress()));

        createHouses.add(new CreateHouse()
            .quantityFlat(150)
            .houseArea(BigDecimal.valueOf(7260))
            .adjoiningArea(600)
            .address(createAddress()));

        return createHouses;
    }

    private List<CreateContact> createContactList() {
        List<CreateContact> createContact = new ArrayList<>();
        createContact.add(new CreateEmailContact()
            .email("primaryemail@example.com")
            .type(ContactType.EMAIL)
            .main(true));

        createContact.add(new CreateEmailContact()
            .email("secondaryemail@example.com")
            .type(ContactType.EMAIL)
            .main(false));
        return createContact;
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
}
