package com.softserveinc.ita.homeproject.api.tests.cooperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.tests.query.CooperationQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.BaseReadView;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class QueryCooperationIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

/*
    @BeforeAll
    void fillDatabaseWithRandomCoopsList() {
        List<CreateCooperation> createCooperationList = createCooperationList();
        createCooperationList.forEach(element -> {
            try {
                cooperationApi.createCooperation(element);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        });
    }
*/

    @Test
    void getAllCooperationAscSort() throws ApiException {
        createCooperationList();

        List<ReadCooperation> queryResponse = new CooperationQuery
            .Builder(cooperationApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllCooperationDescSort() throws ApiException {
        createCooperationList();

        List<ReadCooperation> queryResponse = new CooperationQuery
            .Builder(cooperationApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,desc")
            .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId).reversed());
    }

    @Test
    void getAllCooperationFilteredBy() throws ApiException {
        CreateCooperation expected = createCooperation();
        expected.setName("testedByFilters");
        cooperationApi.createCooperation(expected);

        List<ReadCooperation> queryResponse = new CooperationQuery
            .Builder(cooperationApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .filter("name=like=testedByFilters")
            .build().perform();

        queryResponse.forEach(element -> assertEquals(element.getName(), expected.getName()));
        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllCooperationByName() throws ApiException {
        ReadCooperation expected = cooperationApi.createCooperation(createCooperation());

        List<ReadCooperation> queryResponse = new CooperationQuery
            .Builder(cooperationApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .name(expected.getName())
            .build().perform();

        queryResponse.forEach(element -> assertEquals(element.getName(), expected.getName()));
        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllCooperationByUsreo() throws ApiException {
        ReadCooperation expected = cooperationApi.createCooperation(createCooperation());

        List<ReadCooperation> queryResponse = new CooperationQuery
            .Builder(cooperationApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .usreo(expected.getUsreo())
            .build().perform();

        queryResponse.forEach(element -> assertEquals(element.getUsreo(), expected.getUsreo()));
        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllCooperationByIban() throws ApiException {
        ReadCooperation expected = cooperationApi.createCooperation(createCooperation());

        List<ReadCooperation> queryResponse = new CooperationQuery
            .Builder(cooperationApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .usreo(expected.getIban())
            .build().perform();

        queryResponse.forEach(element -> assertEquals(element.getIban(), expected.getIban()));
        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }


    private CreateCooperation createCooperation() {
        return new CreateCooperation()
            .name(RandomStringUtils.randomAlphabetic(10))
            .usreo(RandomStringUtils.randomAlphabetic(10))
            .iban(RandomStringUtils.randomAlphabetic(20))
            .address(new Address()
                .city("Dnepr")
                .district("District")
                .houseBlock("block")
                .houseNumber("number")
                .region("Dnipro")
                .street("street")
                .zipCode("zipCode"))
            .houses(new ArrayList<>());
    }

    private List<CreateCooperation> createCooperationList() {
        List<CreateCooperation> createCooperations = new ArrayList<>();
        createCooperations.add(createCooperation());
        createCooperations.add(createCooperation());
        createCooperations.add(createCooperation());
        createCooperations.add(createCooperation());
        createCooperations.add(createCooperation());
        return createCooperations;
    }

}