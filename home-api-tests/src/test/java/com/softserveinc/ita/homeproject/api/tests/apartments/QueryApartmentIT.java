package com.softserveinc.ita.homeproject.api.tests.apartments;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.ApartmentApi;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.HouseApi;
import com.softserveinc.ita.homeproject.api.tests.query.ApartmentQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryApartmentIT {
    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getClient());

    @Test
    void getAllApartmentsAscSort() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCoop.getId(), createHouse());

        apartmentApi.createApartment(readHouse.getId(), createApartment());
        apartmentApi.createApartment(readHouse.getId(), createSecondApartment());


        List<ReadApartment> queryResponse = new ApartmentQuery.Builder(apartmentApi)
                .houseId(readHouse.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllApartmentsDescSort() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCoop.getId(), createHouse());

        apartmentApi.createApartment(readHouse.getId(), createApartment());
        apartmentApi.createApartment(readHouse.getId(), createSecondApartment());


        List<ReadApartment> queryResponse = new ApartmentQuery.Builder(apartmentApi)
                .houseId(readHouse.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId).reversed());
    }

    @Test
    void getAllApartmentsFilteredByArea() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCoop.getId(), createHouse());

        apartmentApi.createApartment(readHouse.getId(), createApartment());
        apartmentApi.createApartment(readHouse.getId(), createSecondApartment());

        List<ReadApartment> queryResponse = new ApartmentQuery.Builder(apartmentApi)
                .houseId(readHouse.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .filter("apartmentArea=bt=(60,100)")
                .build().perform();

        queryResponse
                .forEach(element -> assertTrue(Objects.requireNonNull(element.getApartmentArea()).compareTo(BigDecimal.valueOf(60)) > 0 && element.getApartmentArea().compareTo(BigDecimal.valueOf(100)) < 0));
        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllApartmentsByApartmentId() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCoop.getId(), createHouse());

        ReadApartment readApartment = apartmentApi.createApartment(readHouse.getId(), createApartment());

        Long apartmentId = Objects.requireNonNull(readApartment.getId());
        List<ReadApartment> queryResponse = new ApartmentQuery.Builder(apartmentApi)
                .houseId(readHouse.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .id(apartmentId)
                .build().perform();
        queryResponse.forEach(element -> assertEquals(element.getId(), readApartment.getId()));
    }

    @Test
    void getAllApartmentByApartmentArea() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCoop.getId(), createHouse());

        ReadApartment readApartment = apartmentApi.createApartment(readHouse.getId(), createApartment());

        BigDecimal apartmentArea = readApartment.getApartmentArea();
        List<ReadApartment> queryResponse = new ApartmentQuery
                .Builder(apartmentApi)
                .houseId(readHouse.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .apartmentArea(apartmentArea)
                .build().perform();

        queryResponse
                .forEach(element -> assertEquals(element.getApartmentArea(), readApartment.getApartmentArea()));
    }

    @Test
    void getAllApartmentsByApartmentNumber() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadHouse readHouse = houseApi.createHouse(readCoop.getId(), createHouse());

        ReadApartment readApartment = apartmentApi.createApartment(readHouse.getId(), createApartment());

        String apartmentNumber = readApartment.getApartmentNumber();

        List<ReadApartment> queryResponse = new ApartmentQuery
                .Builder(apartmentApi)
                .houseId(readHouse.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .apartmentNumber(apartmentNumber)
                .build().perform();

        queryResponse
                .forEach(element -> assertEquals(element.getApartmentNumber(), readApartment.getApartmentNumber()));
    }

    private CreateApartment createApartment() {
        return new CreateApartment()
                .area(BigDecimal.valueOf(100.5))
                .number("15")
                .invitations(createApartmentInvitation());
    }

    private CreateApartment createSecondApartment() {
        return new CreateApartment()
                .area(BigDecimal.valueOf(65.5))
                .number("16")
                .invitations(createApartmentInvitation());
    }

    private List<CreateInvitation> createApartmentInvitation() {
        List<CreateInvitation> createInvitations = new ArrayList<>();
        createInvitations.add(new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.3))
                .email("invitation@gmail.com")
                .type(InvitationType.APARTMENT));

        createInvitations.add(new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.7))
                .email("invitation2@gmail.com")
                .type(InvitationType.APARTMENT));

        return createInvitations;
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
                .adminEmail("G.Y.Andreevich@gmail.com");
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
