package com.softserveinc.ita.homeproject.api.tests.cooperations;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.UpdateCooperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class CooperationApiIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());


    @Test
    void createCooperationTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        ApiResponse<ReadCooperation> response = cooperationApi.createCooperationWithHttpInfo(createCoop);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertCooperation(createCoop, response.getData());
    }

    @Test
    void getCooperationTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        ReadCooperation savedCooperation = cooperationApi.createCooperation(createCoop);
        ApiResponse<ReadCooperation> response = cooperationApi.getCooperationWithHttpInfo(savedCooperation.getId());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertCooperation(createCoop, response.getData());
    }

    @Test
    void updateCooperationTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        ReadCooperation savedCooperation = cooperationApi.createCooperation(createCoop);

        UpdateCooperation updateCoop = new UpdateCooperation()
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

        ApiResponse<ReadCooperation> response = cooperationApi
            .updateCooperationWithHttpInfo(savedCooperation.getId(), updateCoop);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertCooperation(savedCooperation, updateCoop, response.getData());
    }

    @Test
    void deleteCooperationTest() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        ApiResponse<Void> response = cooperationApi.removeCooperationWithHttpInfo(readCoop.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationApi.getCooperation(readCoop.getId()));
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

    private void assertCooperation(CreateCooperation expected, ReadCooperation actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getIban(), actual.getIban());
        assertEquals(expected.getUsreo(), actual.getUsreo());
        assertEquals(expected.getHouses(), actual.getHouses());
        assertEquals(expected.getAddress(), actual.getAddress());
    }

    private void assertCooperation(ReadCooperation saved, UpdateCooperation update, ReadCooperation updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getName(), updated.getName());
        assertEquals(update.getIban(), updated.getIban());
        assertEquals(update.getUsreo(), updated.getUsreo());
        assertEquals(update.getAddress(), updated.getAddress());
    }


}
