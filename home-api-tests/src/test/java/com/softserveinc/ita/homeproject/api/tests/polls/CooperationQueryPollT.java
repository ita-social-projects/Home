package com.softserveinc.ita.homeproject.api.tests.polls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.api.tests.query.CooperationPollQuery;
import com.softserveinc.ita.homeproject.model.BaseReadView;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import org.junit.jupiter.api.Test;

public class CooperationQueryPollT {

    private final static CooperationPollApi COOPERATION_POLL_API = CooperationPollApiT.COOPERATION_POLL_API;

    @Test
    void getAllPollsAscSort() throws ApiException {

        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiT.COOPERATION_ID, CooperationPollApiT.createPoll());
        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiT.COOPERATION_ID, CooperationPollApiT.createPoll());

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiT.COOPERATION_ID)
            .sort("id,asc")
            .build().perform();

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
    }

    @Test
    void getAllPollsDescSort() throws ApiException {

        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiT.COOPERATION_ID, CooperationPollApiT.createPoll());
        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiT.COOPERATION_ID, CooperationPollApiT.createPoll());

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiT.COOPERATION_ID)
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

        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiT.COOPERATION_ID,
            CooperationPollApiT.createPoll().completionDate(completionDateOne));
        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiT.COOPERATION_ID,
            CooperationPollApiT.createPoll().completionDate(completionDateTwo));

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiT.COOPERATION_ID)
            .sort("id,asc")
            .filter("completion_date=bt=(" + fromDateString + "," + completionDateOneString + ")")
            .build().perform();

        assertEquals(1, queryPoll.size());
        assertEquals(completionDateOne, queryPoll.get(0).getCompletionDate());
    }

    @Test
    void getAllPollsByPollId() throws ApiException {
        Long id = COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiT.COOPERATION_ID, CooperationPollApiT.createPoll()).getId();
        COOPERATION_POLL_API
            .createCooperationPoll(CooperationPollApiT.COOPERATION_ID, CooperationPollApiT.createPoll());

        List<ReadPoll> queryPoll = new CooperationPollQuery.Builder(COOPERATION_POLL_API)
            .cooperationId(CooperationPollApiT.COOPERATION_ID)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .id(id)
            .build().perform();

        assertEquals(1, queryPoll.size());
        assertEquals(id, queryPoll.get(0).getId());
    }
}
