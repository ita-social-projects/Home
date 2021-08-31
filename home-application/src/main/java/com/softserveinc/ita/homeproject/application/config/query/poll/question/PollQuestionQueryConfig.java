package com.softserveinc.ita.homeproject.application.config.query.poll.question;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.application.config.query.base.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.base.QueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.poll.entity.question.PollQuestion;
import org.springframework.stereotype.Component;

@Component
public class PollQuestionQueryConfig implements QueryConfig<PollQuestion> {

    @Override
    public Class<PollQuestion> getEntityClass() {
        return PollQuestion.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(PollQuestionQueryConfig.PollQuestionQueryParamEnum.values());
    }

    public enum PollQuestionQueryParamEnum implements QueryParamEnum {

        ID("id"),
        POLL_ID("poll.id"),
        TYPE("type");


        private final String parameter;

        PollQuestionQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }

    }
}
