package com.softserveinc.ita.homeproject.api.tests.polls;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.PollApi;
import com.softserveinc.ita.homeproject.api.tests.query.PollQuery;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryPollIT implements IQueryPoll {

    private final PollApi POLL_API = CooperationPollApiIT.POLL_API;

    @Override
    public List<ReadPoll> buildQueryPollWithCooperationId(Long cooperationId) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(cooperationId)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithSort(String sort) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .pageNumber(1)
                .pageSize(10)
                .sort(sort)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithFilter(String filter) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .filter(filter)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithPollIdAndCooperationId(Long pollId, Long cooperationId) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(cooperationId)
                .id(pollId)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithType(PollType type) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .pageNumber(1)
                .pageSize(10)
                .type(type)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithStatus(PollStatus status) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .status(status)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithCompletionDate(LocalDateTime completionDate) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .completionDate(completionDate)
                .build().perform();
    }

    @Test
    void getAllPollsOnlyByPollId() throws ApiException {

        Long id = createPoll().getId();

        List<ReadPoll> queryPoll = new PollQuery.Builder(POLL_API)
                .id(id)
                .build()
                .perform();

        assertEquals(1, queryPoll.size());
        assertEquals(id, queryPoll.get(0).getId());
    }
}
