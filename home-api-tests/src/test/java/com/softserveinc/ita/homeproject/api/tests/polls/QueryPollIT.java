package com.softserveinc.ita.homeproject.api.tests.polls;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.PollApi;
import com.softserveinc.ita.homeproject.api.tests.query.PollQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryPollIT extends QueryPoll {

    private final PollApi POLL_API = new PollApi(ApiClientUtil.getClient());

    @Override
    List<ReadPoll> buildQueryPollWithCooperationId(Long cooperationId) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(cooperationId)
                .build().perform();
    }

    @Override
    List<ReadPoll> buildQueryPollWithSort(String sort) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort(sort)
                .build().perform();
    }

    @Override
    List<ReadPoll> buildQueryPollWithFilter(String filter) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .filter(filter)
                .build().perform();
    }

    @Override
    List<ReadPoll> buildQueryPollWithPollIdAndCooperationId(Long pollId, Long cooperationId) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(cooperationId)
                .id(pollId)
                .build().perform();
    }

    @Override
    List<ReadPoll> buildQueryPollWithType(PollType type) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .type(type)
                .build().perform();
    }

    @Override
    List<ReadPoll> buildQueryPollWithStatus(PollStatus status) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .status(status)
                .build().perform();
    }

    @Override
    List<ReadPoll> buildQueryPollWithCompletionDate(LocalDateTime completionDate) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .completionDate(completionDate)
                .build().perform();
    }

    List<ReadPoll> buildQueryPollOnlyByPollId(Long id) throws ApiException {
        return new PollQuery.Builder(POLL_API)
                .id(id)
                .build()
                .perform();
    }

    @Test
    @Override
    void getAllPollsFromNotExistingCooperation() throws ApiException {

        createPoll();
        Long wrongCooperationId = 99999999999L;

        List<ReadPoll> queryPoll = buildQueryPollWithCooperationId(wrongCooperationId);

        assertEquals(0, queryPoll.size());
    }

    @Disabled("Correct exception message is not ready yet. Created task#250.")
    @Test
    void getAllPollsOnlyByPollId() throws ApiException {

        Long id = createPoll().getId();

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> buildQueryPollOnlyByPollId(id))
                .matches(exception -> exception.getCode() == 400)
                .withMessageContaining("Parameter `cooperation_id` is invalid - must not be null.");
    }
}
