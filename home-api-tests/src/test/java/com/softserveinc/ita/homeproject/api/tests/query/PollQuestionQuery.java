package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.ApartmentApi;
import com.softserveinc.ita.homeproject.api.PollQuestionApi;
import com.softserveinc.ita.homeproject.model.ReadApartment;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.ReadQuestion;

import java.math.BigDecimal;
import java.util.List;

public class PollQuestionQuery extends BaseQuery {

    private Long pollId;

    private PollQuestionApi pollQuestionApi;

    private Long id;

    public void setPollQuestionApi(PollQuestionApi pollQuestionApi) {
        this.pollQuestionApi = pollQuestionApi;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }


    public List<ReadQuestion> perform() throws ApiException {
        return pollQuestionApi
                .queryQuestion(pollId, this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        id);
    }

    public static class Builder extends BaseQuery.BaseBuilder<PollQuestionQuery, PollQuestionQuery.Builder> {

        public Builder(PollQuestionApi pollQuestionApi) {
            queryClass.setPollQuestionApi(pollQuestionApi);
        }


        public PollQuestionQuery.Builder pollId(Long pollId) {
            queryClass.setPollId(pollId);
            return this;
        }

        @Override
        protected PollQuestionQuery getActual() {
            return new PollQuestionQuery();
        }

        @Override
        protected PollQuestionQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
