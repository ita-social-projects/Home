package com.softserveinc.ita.homeproject.api.tests.polls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.api.PolledHouseApi;
import com.softserveinc.ita.homeproject.api.tests.query.HousePollQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import org.junit.jupiter.api.Test;

class QueryHousePollIT {
    final CooperationPollApi COOPERATION_POLL_API = new CooperationPollApi(ApiClientUtil.getClient());

    final PolledHouseApi POLLED_HOUSE_API = new PolledHouseApi(ApiClientUtil.getClient());

    @Test
    void getAllHousesFromPollsAscSort() throws ApiException {
        ReadPoll readPoll = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadHouse> queryHouse = new HousePollQuery.Builder(POLLED_HOUSE_API)
            .pollId(readPoll.getId())
            .sort("id,asc")
            .build().perform();

        assertThat(queryHouse).isSortedAccordingTo(Comparator.comparing(ReadHouse::getId));
    }

    @Test
    void getAllHousesFromPollsDescSort() throws ApiException {
        ReadPoll readPoll = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadHouse> queryHouse = new HousePollQuery.Builder(POLLED_HOUSE_API)
            .pollId(readPoll.getId())
            .sort("id,desc")
            .build().perform();

        assertThat(queryHouse).isSortedAccordingTo(Comparator.comparing(ReadHouse::getId).reversed());
    }

    @Test
    void getAllHousesFromPollsById() throws ApiException {
        ReadPoll readPoll = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        Long houseId = readPoll.getPolledHouses().get(0).getId();

        List<ReadHouse> queryHouse = new HousePollQuery.Builder(POLLED_HOUSE_API)
            .pollId(readPoll.getId())
            .sort("id,desc")
            .id(houseId)
            .build().perform();

        assertEquals(1, queryHouse.size());
        assertEquals(houseId, queryHouse.get(0).getId());
    }

    @Test
    void getAllHousesFromPollsByQuantityFlat() throws ApiException {
        ReadPoll readPoll = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        Integer quantityFlat = readPoll.getPolledHouses().get(0).getQuantityFlat();

        List<ReadHouse> queryHouse = new HousePollQuery.Builder(POLLED_HOUSE_API)
            .pollId(readPoll.getId())
            .sort("id,asc")
            .quantityFlat(quantityFlat)
            .build().perform();

        assertEquals(2, queryHouse.size());
        assertEquals(quantityFlat, queryHouse.get(0).getQuantityFlat());
    }

    @Test
    void getAllHousesFromPollsByAdjoiningArea() throws ApiException {
        ReadPoll readPoll = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        Integer adjoiningArea = readPoll.getPolledHouses().get(0).getAdjoiningArea();

        List<ReadHouse> queryHouse = new HousePollQuery.Builder(POLLED_HOUSE_API)
            .pollId(readPoll.getId())
            .sort("id,asc")
            .adjoiningArea(adjoiningArea)
            .build().perform();

        assertEquals(2, queryHouse.size());
        assertEquals(adjoiningArea, queryHouse.get(0).getAdjoiningArea());
    }

    @Test
    void getAllHousesFromPollsByHouseArea() throws ApiException {
        ReadPoll readPoll = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        BigDecimal houseArea = readPoll.getPolledHouses().get(0).getHouseArea();

        List<ReadHouse> queryHouse = new HousePollQuery.Builder(POLLED_HOUSE_API)
            .pollId(readPoll.getId())
            .sort("id,asc")
            .houseArea(houseArea)
            .build().perform();

        assertEquals(2, queryHouse.size());
        assertEquals(houseArea, queryHouse.get(0).getHouseArea());
    }
}
