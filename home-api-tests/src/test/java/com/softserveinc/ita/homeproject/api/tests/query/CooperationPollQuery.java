package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.client.model.PollStatus;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.ReadPoll;
import com.softserveinc.ita.homeproject.client.model.ReadShortenPoll;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CooperationPollQuery extends BaseQuery {

    private Long cooperationId;

    private LocalDateTime creationDate;

    private LocalDateTime completionDate;

    private PollStatus status;

    private String description;

    private PollType type;

    private CooperationPollApi cooperationPollApi;

    public List<ReadShortenPoll> perform() throws ApiException {
        return cooperationPollApi
                .queryCooperationPoll(cooperationId,
                        this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        this.getId(),
                        this.getCreationDate(),
                        this.getCompletionDate(),
                        this.getDescription(),
                        this.getType());
    }

    public static class Builder extends BaseBuilder<CooperationPollQuery, CooperationPollQuery.Builder> {

        public Builder(CooperationPollApi cooperationPollApi) {
            queryClass.setCooperationPollApi(cooperationPollApi);
        }


        public CooperationPollQuery.Builder cooperationId(Long cooperationId) {
            queryClass.setCooperationId(cooperationId);
            return this;
        }

        public CooperationPollQuery.Builder creationDate(LocalDateTime creationDate) {
            queryClass.setCreationDate(creationDate);
            return this;
        }

        public CooperationPollQuery.Builder completionDate(LocalDateTime completionDate) {
            queryClass.setCompletionDate(completionDate);
            return this;
        }

        public CooperationPollQuery.Builder type(PollType pollType) {
            queryClass.setType(pollType);
            return this;
        }

        public CooperationPollQuery.Builder status(PollStatus pollStatus) {
            queryClass.setStatus(pollStatus);
            return this;
        }

        @Override
        protected CooperationPollQuery getActual() {
            return new CooperationPollQuery();
        }

        @Override
        protected CooperationPollQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
