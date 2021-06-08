package com.softserveinc.ita.homeproject.api.tests.polls;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.api.PollApi;
import com.softserveinc.ita.homeproject.api.tests.query.PollQuery;
import com.softserveinc.ita.homeproject.model.BaseReadView;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryPollIT {

    private final CooperationPollApi COOPERATION_POLL_API = CooperationPollApiIT.COOPERATION_POLL_API;
    private final PollApi POLL_API = CooperationPollApiIT.POLL_API;

    @Test
    void getAllPollsAscSort() throws ApiException {

        COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());
        COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .sort("id,asc")
                .build().perform();

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllPollsDescSort() throws ApiException {

        COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());
        COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .sort("id,desc")
                .build().perform();

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId).reversed());
    }

    @Test
    void getAllPollsByPollId() throws ApiException {
        Long id = COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll()).getId();
        COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new PollQuery.Builder(POLL_API)
                .pageNumber(1)
                .pageSize(10)
                .id(id)
                .build().perform();

        assertEquals(1, queryPoll.size());
        assertEquals(id, queryPoll.get(0).getId());
    }

    @Test
    void getAllPollsByPollIdAndCooperationId() throws ApiException {
        Long id = COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll()).getId();
        COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .id(id)
                .build().perform();

        assertEquals(1, queryPoll.size());
        assertEquals(id, queryPoll.get(0).getId());
    }

    @Test
    void getAllPollsByPollIdWithNonRelatedCooperation() throws ApiException {

        Long id = COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll()).getId();

        List<ReadPoll> queryPoll = new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.SECOND_COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .id(id)
                .build().perform();

        assertEquals(0, queryPoll.size());
    }

    @Test
    void getAllPollsFromNonRelatedCooperation() throws ApiException {

        COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.SECOND_COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .build().perform();

        assertEquals(0, queryPoll.size());
    }
}
