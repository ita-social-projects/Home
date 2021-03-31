package com.softserveinc.ita.homeproject.api.tests.houses;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.HouseApi;
import com.softserveinc.ita.homeproject.api.tests.query.HouseQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.BaseReadView;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class QueryHouseIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getClient());

    @Test
    void getAllHousesAscSort() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllHousesDescSort() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,desc")
            .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId).reversed());
    }

    @Test
    void getAllHousesFilteredBy() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .filter("quantityFlat=bt=(1,200)")
            .build().perform();

        queryResponse
            .forEach(element -> assertTrue(element.getQuantityFlat() >= 1 && element.getQuantityFlat() <= 200));
        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllHousesByHouseId() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        Long houseId = Objects.requireNonNull(readCoop.getHouses()).get(0).getId();
        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .houseId(houseId)
            .build().perform();

        queryResponse.forEach(element -> assertEquals(element.getId(), readCoop.getHouses().get(0).getId()));
    }

    @Test
    void getAllHousesByQuantityFlat() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        Integer quantityFlat = Objects.requireNonNull(readCoop.getHouses()).get(0).getQuantityFlat();
        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .quantityFlat(quantityFlat)
            .build().perform();

        queryResponse
            .forEach(element -> assertEquals(element.getQuantityFlat(), readCoop.getHouses().get(0).getQuantityFlat()));
    }

    @Test
    void getAllHousesByHouseArea() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        BigDecimal houseArea = Objects.requireNonNull(readCoop.getHouses()).get(0).getHouseArea();
        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .houseArea(houseArea)
            .build().perform();

        queryResponse
            .forEach(element -> assertEquals(element.getHouseArea(), readCoop.getHouses().get(0).getHouseArea()));
    }

    @Test
    void getAllHousesByAdjoiningArea() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        Integer adjoiningArea = Objects.requireNonNull(readCoop.getHouses()).get(0).getAdjoiningArea();

        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .adjoiningArea(adjoiningArea)
            .build().perform();

        queryResponse
            .forEach(element -> assertEquals(element.getAdjoiningArea(), readCoop.getHouses().get(0).getAdjoiningArea()));
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
