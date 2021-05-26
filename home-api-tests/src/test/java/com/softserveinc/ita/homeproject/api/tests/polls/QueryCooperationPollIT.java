package com.softserveinc.ita.homeproject.api.tests.polls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.api.tests.query.CooperationPollQuery;
import com.softserveinc.ita.homeproject.model.BaseReadView;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import com.softserveinc.ita.homeproject.model.UpdatePoll;
import org.junit.jupiter.api.Test;

class QueryCooperationPollIT {

    private final CooperationPollApi COOPERATION_POLL_API = CooperationPollApiIT.COOPERATION_POLL_API;

    @Test
    void getAllPollsAscSort() throws ApiException {

        COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());
        COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
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

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiIT.COOPERATION_ID)
            .sort("id,desc")
            .build().perform();

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId).reversed());
    }

    @Test
    void getAllPollsFilteredByCompletionDate() throws ApiException {
        LocalDateTime completionDateOne = LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime completionDateTwo = LocalDateTime.now().plusDays(15).truncatedTo(ChronoUnit.MINUTES);
        String completionDateOneString = completionDateOne.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String fromDateString = completionDateOne
            .minusDays(1)
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiIT.COOPERATION_ID,
            CooperationPollApiIT.createPoll().completionDate(completionDateOne));
        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiIT.COOPERATION_ID,
            CooperationPollApiIT.createPoll().completionDate(completionDateTwo));

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiIT.COOPERATION_ID)
            .sort("id,asc")
            .filter("completion_date=bt=(" + fromDateString + "," + completionDateOneString + ")")
            .build().perform();

        assertEquals(1, queryPoll.size());
        assertEquals(completionDateOne, queryPoll.get(0).getCompletionDate());
    }

    @Test
    void getAllPollsByPollId() throws ApiException {
        Long id = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll()).getId();
        COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiIT.COOPERATION_ID)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .id(id)
            .build().perform();

        assertEquals(1, queryPoll.size());
        assertEquals(id, queryPoll.get(0).getId());
    }

    @Test
    void getAllPollsByPollType() throws ApiException {
        COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiIT.COOPERATION_ID)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .type(PollType.SIMPLE)
            .build().perform();

        assertTrue(queryPoll.size() > 0);
        queryPoll.forEach(poll -> assertEquals(PollType.SIMPLE, poll.getType()));
    }

    @Test
    void getAllPollsByPollStatus() throws ApiException {
        ReadPoll readPoll = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());
        UpdatePoll updatePoll = CooperationPollApiIT.updatePoll();
        COOPERATION_POLL_API.updateCooperationPoll(CooperationPollApiIT.COOPERATION_ID, readPoll.getId(), updatePoll);

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiIT.COOPERATION_ID)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .status(PollStatus.SUSPENDED)
            .build().perform();

        assertTrue(queryPoll.size() > 0);
        queryPoll.forEach(poll -> assertEquals(PollStatus.SUSPENDED, poll.getStatus()));
    }
}
