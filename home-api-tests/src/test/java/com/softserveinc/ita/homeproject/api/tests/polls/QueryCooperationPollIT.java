package com.softserveinc.ita.homeproject.api.tests.polls;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.api.tests.query.CooperationPollQuery;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.model.PollStatus;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.ReadShortenPoll;
import com.softserveinc.ita.homeproject.client.model.ReadShortenPoll;
import org.junit.jupiter.api.Test;

public class QueryCooperationPollIT extends QueryShortenPoll {

    @Override
    List<ReadShortenPoll> buildQueryPollWithCooperationId(Long cooperationId) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(cooperationId)
                .build().perform();
    }

    @Override
    List<ReadShortenPoll> buildQueryPollWithSort(String sort) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .sort(sort)
                .build().perform();
    }

    @Override
    List<ReadShortenPoll> buildQueryPollWithFilter(String filter) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .filter(filter)
                .build().perform();
    }

    @Override
    List<ReadShortenPoll> buildQueryPollWithPollIdAndCooperationId(Long pollId, Long cooperationId) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(cooperationId)
                .id(pollId)
                .build().perform();
    }

    @Override
    List<ReadShortenPoll> buildQueryPollWithType(PollType type) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .type(type)
                .build().perform();
    }

    @Override
    List<ReadShortenPoll> buildQueryPollWithStatus(PollStatus status) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .status(status)
                .build().perform();
    }

    @Override
    List<ReadShortenPoll> buildQueryPollWithCompletionDate(LocalDateTime completionDate) throws ApiException {
        return new CooperationPollQuery.Builder(COOPERATION_POLL_API)
                .cooperationId(CooperationPollApiIT.COOPERATION_ID)
                .completionDate(completionDate)
                .build().perform();
    }

    @Test
    @Override
    void getAllPollsFromNotExistingCooperation() throws ApiException {

        createPoll();
        Long wrongCooperationId = 999999999L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> buildQueryPollWithCooperationId(wrongCooperationId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Cooperation with 'id: " + wrongCooperationId + "' is not found");
    }
}
