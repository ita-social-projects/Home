package com.softserveinc.ita.homeproject.api.tests.polls;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.client.model.PollStatus;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.ReadPoll;
import com.softserveinc.ita.homeproject.client.model.ReadShortenPoll;
import com.softserveinc.ita.homeproject.client.model.ReadShortenPoll;
import com.softserveinc.ita.homeproject.client.model.UpdatePoll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class QueryShortenPoll {

    final CooperationPollApi COOPERATION_POLL_API = new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

    abstract List<ReadShortenPoll> buildQueryPollWithCooperationId(Long id) throws ApiException;
    abstract List<ReadShortenPoll> buildQueryPollWithSort(String sort) throws ApiException;
    abstract List<ReadShortenPoll> buildQueryPollWithFilter(String filter) throws ApiException;
    abstract List<ReadShortenPoll> buildQueryPollWithPollIdAndCooperationId(Long id, Long cooperationId) throws ApiException;
    abstract List<ReadShortenPoll> buildQueryPollWithType(PollType type) throws ApiException;
    abstract List<ReadShortenPoll> buildQueryPollWithStatus(PollStatus status) throws ApiException;
    abstract List<ReadShortenPoll> buildQueryPollWithCompletionDate(LocalDateTime completionDate) throws ApiException;

    ReadPoll createPoll() throws ApiException {
        return COOPERATION_POLL_API
                .createCooperationPoll(CooperationPollApiIT.COOPERATION_ID, CooperationPollApiIT.createPoll());
    }

    private void createPollWithCompletionDate(LocalDateTime completionDate) throws ApiException {
        COOPERATION_POLL_API.createCooperationPoll(CooperationPollApiIT.COOPERATION_ID,
                CooperationPollApiIT.createPoll().completionDate(completionDate));
    }

    private void assertCompletionDateInRange(List<ReadShortenPoll> polls, LocalDateTime fromDate, LocalDateTime toDate){
        assertTrue(polls.size() > 0);
        polls.forEach(poll -> {
            assertTrue(Objects.requireNonNull(poll.getCompletionDate()).compareTo(fromDate)>=0);
            assertTrue(Objects.requireNonNull(poll.getCompletionDate()).compareTo(toDate)<=0);
        });
    }

    private void assertCreationDateInRange(List<ReadShortenPoll> polls, LocalDateTime fromDate, LocalDateTime toDate){
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

        List<ReadShortenPoll> queryPoll = buildQueryPollWithSort("id,asc");

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(ReadShortenPoll::getId));
    }

    @Test
    void getAllPollsDescSort() throws ApiException {

        createPoll();
        createPoll();

        List<ReadShortenPoll> queryPoll = buildQueryPollWithSort("id,desc");

        assertThat(queryPoll).isSortedAccordingTo(Comparator.comparing(ReadShortenPoll::getId).reversed());
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

        List<ReadShortenPoll> queryPoll = buildQueryPollWithFilter("completion_date=bt=(" + fromDateString + "," + completionDateTwoString + ")");

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

        List<ReadShortenPoll> queryPoll = buildQueryPollWithFilter("creation_date=bt=(" + fromDateString + "," + toDateString + ")");

        assertCreationDateInRange(queryPoll,fromDate,toDate);
    }

    @Test
    void getAllPollsByCompletionDate() throws ApiException {

        LocalDateTime completionDateOne = LocalDateTime.now().plusDays(15).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime completionDateTwo = LocalDateTime.now().plusDays(15).truncatedTo(ChronoUnit.MINUTES);

        createPollWithCompletionDate(completionDateOne);
        createPollWithCompletionDate(completionDateTwo);

        List<ReadShortenPoll> queryPoll = buildQueryPollWithCompletionDate(completionDateOne);

        assertTrue(queryPoll.size() > 0);
        queryPoll.forEach(poll -> assertEquals(completionDateOne, poll.getCompletionDate()));
    }

    @Test
    void getAllPollsByPollType() throws ApiException {

        createPoll();

        List<ReadShortenPoll> queryPoll = buildQueryPollWithType(PollType.SIMPLE);

        assertTrue(queryPoll.size() > 0);
        queryPoll.forEach(poll -> assertEquals(PollType.SIMPLE, poll.getType()));
    }

    @Test
    void getAllPollsByPollStatus() throws ApiException {

        ReadPoll ReadShortenPoll = createPoll();
        UpdatePoll updatePoll = CooperationPollApiIT.updatePoll();
        COOPERATION_POLL_API.updateCooperationPoll(CooperationPollApiIT.COOPERATION_ID, ReadShortenPoll.getId(), updatePoll);

        List<ReadShortenPoll> queryPoll = buildQueryPollWithStatus(PollStatus.SUSPENDED);

        assertTrue(queryPoll.size() > 0);
    }

    @Test
    void getAllPollsByPollIdAndCooperationId() throws ApiException {

        Long id = createPoll().getId();

        List<ReadShortenPoll> queryPoll = buildQueryPollWithPollIdAndCooperationId(id, CooperationPollApiIT.COOPERATION_ID);

        assertEquals(1, queryPoll.size());
        assertEquals(id, queryPoll.get(0).getId());
    }

    @Test
    void getAllPollsByPollIdWithNonRelatedCooperation() throws ApiException {

        Long pollId = createPoll().getId();
        Long cooperationId = CooperationPollApiIT.SECOND_COOPERATION_ID;

        List<ReadShortenPoll> queryPoll = buildQueryPollWithPollIdAndCooperationId(pollId, cooperationId);

        assertEquals(0, queryPoll.size());
    }

    @Test
    void getAllPollsFromNonRelatedCooperation() throws ApiException{

        createPoll();

        List<ReadShortenPoll> queryPoll = buildQueryPollWithCooperationId(CooperationPollApiIT.SECOND_COOPERATION_ID);

        assertEquals(0, queryPoll.size());
    }

    abstract void getAllPollsFromNotExistingCooperation() throws ApiException;
    
}
