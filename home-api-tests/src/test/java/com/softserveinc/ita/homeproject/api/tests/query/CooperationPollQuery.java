package com.softserveinc.ita.homeproject.api.tests.query;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CooperationPollQuery extends BaseQuery {

    private Long cooperationId;

    private LocalDateTime creationDate;

    private LocalDateTime completionDate;

    private PollStatus status;

    private PollType type;

    private CooperationPollApi cooperationPollApi;

    public List<ReadPoll> perform() throws ApiException {
        return cooperationPollApi
            .queryCooperationPoll(cooperationId,
                this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                this.getId(),
                this.getCreationDate(),
                this.getCompletionDate(),
                this.getType(),
                this.getStatus());
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
