package com.softserveinc.ita.homeproject.api.tests.polls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.client.model.PollStatus;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.ReadPoll;
import com.softserveinc.ita.homeproject.client.model.UpdatePoll;
import org.junit.jupiter.api.Test;

abstract class QueryPoll {

    final CooperationPollApi COOPERATION_POLL_API = new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

    abstract List<ReadPoll> buildQueryPollWithCooperationId(Long id) throws ApiException;
    abstract List<ReadPoll> buildQueryPollWithSort(String sort) throws ApiException;
    abstract List<ReadPoll> buildQueryPollWithFilter(String filter) throws ApiException;
    abstract List<ReadPoll> buildQueryPollWithPollIdAndCooperationId(Long id, Long cooperationId) throws ApiException;
    abstract List<ReadPoll> buildQueryPollWithType(PollType type) throws ApiException;
    abstract List<ReadPoll> buildQueryPollWithStatus(PollStatus status) throws ApiException;
    abstract List<ReadPoll> buildQueryPollWithCompletionDate(LocalDateTime completionDate) throws ApiException;

    ReadPoll createPoll() throws ApiException {
        return COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());
    }

    private void createPollWithCompletionDate(LocalDateTime completionDate) throws ApiException {
        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiIT.COOPERATION_ID,
                CooperationPollApiIT.createPollWithCompletionDate(completionDate));
    }

    private ReadPoll createPollWithCreationDate(LocalDateTime creationDate) throws ApiException {
       return COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiIT.COOPERATION_ID,
            CooperationPollApiIT.createPollWithCreationDate(creationDate));
    }

    private void assertCompletionDateInRange(List<ReadPoll> polls, LocalDateTime fromDate, LocalDateTime toDate){
        assertTrue(polls.size() > 0);
        polls.forEach(poll -> {
            assertTrue(Objects.requireNonNull(poll.getCompletionDate()).compareTo(fromDate)>=0);
            assertTrue(Objects.requireNonNull(poll.getCompletionDate()).compareTo(toDate)<=0);
        });
    }

    private void assertCreationDateInRange(List<ReadPoll> polls, LocalDateTime fromDate, LocalDateTime toDate){
        assertTrue(polls.size() > 0);
        polls.forEach(poll -> {
            assertTrue(Objects.requireNonNull(poll.getCreationDate()).compareTo(fromDate) >= 0);
            assertTrue(Objects.requireNonNull(poll.getCreationDate()).compareTo(toDate) <= 0);
        });
    }

    @Test
    void getAllPollsAscSort() throws ApiException {

        createPoll();
        createPoll();

        List<ReadPoll> queryPoll = buildQueryPollWithSort("id,asc");

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(ReadPoll::getId));
    }

    @Test
    void getAllPollsDescSort() throws ApiException {

        createPoll();
        createPoll();

        List<ReadPoll> queryPoll = buildQueryPollWithSort("id,desc");

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(ReadPoll::getId).reversed());
    }

    @Test
    void getAllPollsFilteredByCompletionDate() throws ApiException {

        LocalDateTime completionDateOne = LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime completionDateTwo = LocalDateTime.now().plusDays(15).truncatedTo(ChronoUnit.MINUTES);
        String completionDateOneString = completionDateOne.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String completionDateTwoString = completionDateTwo.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime fromDate = completionDateOne.minusDays(1);
        String fromDateString = fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        createPollWithCompletionDate(completionDateOne);
        createPollWithCompletionDate(completionDateTwo);

        List<ReadPoll> queryPoll = buildQueryPollWithFilter("completion_date=bt=(" + fromDateString + "," + completionDateTwoString + ")");

        assertCompletionDateInRange(queryPoll, fromDate, completionDateTwo);
    }

    @Test
    void getAllPollsFilteredByCreationDate() throws ApiException {

        createPoll();
        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusDays(2);
        String fromDateString = fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String toDateString = toDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        createPoll();

        List<ReadPoll> queryPoll = buildQueryPollWithFilter("creation_date=bt=(" + fromDateString + "," + toDateString + ")");

        assertCreationDateInRange(queryPoll,fromDate,toDate);
    }

    @Test
    void getAllPollsByCompletionDate() throws ApiException {

        LocalDateTime completionDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        createPollWithCompletionDate(completionDate);
        createPollWithCompletionDate(completionDate);

        List<ReadPoll> queryPoll = buildQueryPollWithCompletionDate(completionDate.plusDays(15L));

        assertTrue(queryPoll.size() > 0);
        queryPoll.forEach(poll -> assertEquals(completionDate.plusDays(15L), poll.getCompletionDate()));
    }

    @Test
    void getAllPollsByPollType() throws ApiException {

        createPoll();

        List<ReadPoll> queryPoll = buildQueryPollWithType(PollType.SIMPLE);

        assertTrue(queryPoll.size() > 0);
        queryPoll.forEach(poll -> assertEquals(PollType.SIMPLE, poll.getType()));
    }

    @Test
    void getAllPollsByPollStatus() throws ApiException {

        LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusDays(1);
        ReadPoll readPoll = createPollWithCreationDate(creationDate);
        UpdatePoll updatePoll = CooperationPollApiIT.updatePollWithCreationDate(creationDate);
        COOPERATION_POLL_API.updateCooperationPoll(CooperationPollApiIT.COOPERATION_ID, readPoll.getId(), updatePoll);

        List<ReadPoll> queryPoll = buildQueryPollWithStatus(PollStatus.DRAFT);

        assertTrue(queryPoll.size() > 0);
        queryPoll.forEach(poll -> assertEquals(PollStatus.DRAFT, poll.getStatus()));
    }

    @Test
    void getAllPollsByPollIdAndCooperationId() throws ApiException {

        Long id = createPoll().getId();

        List<ReadPoll> queryPoll = buildQueryPollWithPollIdAndCooperationId(id, CooperationPollApiIT.COOPERATION_ID);

        assertEquals(1, queryPoll.size());
        assertEquals(id, queryPoll.get(0).getId());
    }

    @Test
    void getAllPollsByPollIdWithNonRelatedCooperation() throws ApiException {

        Long pollId = createPoll().getId();
        Long cooperationId = CooperationPollApiIT.SECOND_COOPERATION_ID;

        List<ReadPoll> queryPoll = buildQueryPollWithPollIdAndCooperationId(pollId, cooperationId);

        assertEquals(0, queryPoll.size());
    }

    @Test
    void getAllPollsFromNonRelatedCooperation() throws ApiException{

        createPoll();

        List<ReadPoll> queryPoll = buildQueryPollWithCooperationId(CooperationPollApiIT.SECOND_COOPERATION_ID);

        assertEquals(0, queryPoll.size());
    }

    abstract void getAllPollsFromNotExistingCooperation() throws ApiException;
}
