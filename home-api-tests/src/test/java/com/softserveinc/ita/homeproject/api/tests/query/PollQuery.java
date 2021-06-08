package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.PollApi;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PollQuery extends BaseQuery {

    private Long cooperationId;

    private PollApi pollApi;

    public List<ReadPoll> perform() throws ApiException {
        return pollApi
                .queryPoll(
                        this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        this.getCooperationId(),
                        this.getId());
    }

    public static class Builder extends BaseBuilder<PollQuery, PollQuery.Builder> {

        public Builder(PollApi pollApi) {
            queryClass.setPollApi(pollApi);
        }


        public PollQuery.Builder cooperationId(Long cooperationId) {
            queryClass.setCooperationId(cooperationId);
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
