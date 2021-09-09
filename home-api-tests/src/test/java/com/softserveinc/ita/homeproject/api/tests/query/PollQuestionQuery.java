package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.ApartmentApi;
import com.softserveinc.ita.homeproject.client.api.PollQuestionApi;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.QuestionType;
import com.softserveinc.ita.homeproject.client.model.ReadApartment;
import com.softserveinc.ita.homeproject.client.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.ReadQuestion;

import java.math.BigDecimal;
import java.util.List;

public class PollQuestionQuery extends BaseQuery {

    private Long pollId;

    private PollQuestionApi pollQuestionApi;

    private QuestionType type;

    private Long id;

    public void setType(QuestionType type){
        this.type = type;
    }

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
                        id,
                        type);
    }

    public static class Builder extends BaseQuery.BaseBuilder<PollQuestionQuery, PollQuestionQuery.Builder> {

        public Builder(PollQuestionApi pollQuestionApi) {
            queryClass.setPollQuestionApi(pollQuestionApi);
        }


        public PollQuestionQuery.Builder pollId(Long pollId) {
            queryClass.setPollId(pollId);
            return this;
        }

        public PollQuestionQuery.Builder type(QuestionType questionType) {
            queryClass.setType(questionType);
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
