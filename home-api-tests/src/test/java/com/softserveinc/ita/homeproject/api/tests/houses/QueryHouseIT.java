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
import com.softserveinc.ita.homeproject.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class QueryHouseIT {

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
    void getAllHousesById() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        Long id = Objects.requireNonNull(readCoop.getHouses()).get(0).getId();
        List<ReadHouse> queryResponse = new HouseQuery
            .Builder(houseApi)
            .cooperationId(readCoop.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .id(id)
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
            .adminEmail("G.Y.Andreevich@gmail.com")
            .address(createAddress())
            .houses(createHouseList())
            .contacts(createContactList());
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
