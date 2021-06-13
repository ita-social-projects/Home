package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.PollApi;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PollQuery extends BaseQuery {

    private Long cooperationId;

    private LocalDateTime creationDate;

    private LocalDateTime completionDate;

    private PollStatus status;

    private PollType type;

    private PollApi pollApi;

    public List<ReadPoll> perform() throws ApiException {
        return pollApi
                .queryPoll(
                        this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        this.getCooperationId(),
                        this.getId(),
                        this.getCreationDate(),
                        this.getCompletionDate(),
                        this.getType(),
                        this.getStatus());
    }

    public static class Builder extends BaseBuilder<PollQuery, PollQuery.Builder> {

        public Builder(PollApi pollApi) {
            queryClass.setPollApi(pollApi);
        }

        public PollQuery.Builder cooperationId(Long cooperationId) {
            queryClass.setCooperationId(cooperationId);
            return this;
        }

        public PollQuery.Builder creationDate(LocalDateTime creationDate) {
            queryClass.setCreationDate(creationDate);
            return this;
        }

        public PollQuery.Builder completionDate(LocalDateTime completionDate) {
            queryClass.setCompletionDate(completionDate);
            return this;
        }

        public PollQuery.Builder type(PollType pollType) {
            queryClass.setType(pollType);
            return this;
        }

        public PollQuery.Builder status(PollStatus pollStatus) {
            queryClass.setStatus(pollStatus);
            return this;
        }

        @Override
        protected PollQuery getActual() {
            return new PollQuery();
        }

        @Override
        protected PollQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
