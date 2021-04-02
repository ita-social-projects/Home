package com.softserveinc.ita.homeproject.api.tests.houses;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.HouseApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import com.softserveinc.ita.homeproject.model.UpdateHouse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class HouseApiIT {

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

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

        UpdateHouse updateHouse = new UpdateHouse()
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

        ApiResponse<ReadHouse> response =
            houseApi.updateHouseWithHttpInfo(expectedCoop.getId(), expectedHouse.getId(), updateHouse);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertHouse(expectedHouse, updateHouse, response.getData());
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
            .name("newCooperationTest")
            .usreo(RandomStringUtils.randomAlphabetic(10))
            .iban(RandomStringUtils.randomAlphabetic(20))
            .address(createAddress())
            .houses(createHouseList());
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
