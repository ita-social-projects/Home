package com.softserveinc.ita.homeproject.api.tests.polls;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.tests.query.CooperationPollQuery;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class QueryCooperationPollIT implements IQueryPoll {

    @Override
    public List<ReadPoll> buildQueryPollWithCooperationId(Long cooperationId) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(cooperationId)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithSort(String sort) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .sort(sort)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithFilter(String filter) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .filter(filter)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithPollIdAndCooperationId(Long pollId, Long cooperationId) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(cooperationId)
                .id(pollId)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithType(PollType type) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .type(type)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithStatus(PollStatus status) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .status(status)
                .build().perform();
    }

    @Override
    public List<ReadPoll> buildQueryPollWithCompletionDate(LocalDateTime completionDate) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .completionDate(completionDate)
                .build().perform();
    }

    @Disabled("Should sent exception. Is not ready yet. Created task#251.")
    @Test
    @Override
    public void getAllPollsFromNotExistingCooperation() throws ApiException {

        createPoll();
        Long wrongCooperationId = 99999999999L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> buildQueryPollWithCooperationId(wrongCooperationId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Cooperation with 'id: " + wrongCooperationId + "' is not found");
    }
}
