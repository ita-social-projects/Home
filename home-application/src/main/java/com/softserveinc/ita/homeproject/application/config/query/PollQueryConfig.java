package com.softserveinc.ita.homeproject.application.config.query;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import org.springframework.stereotype.Component;

@Component
public class PollQueryConfig implements QueryConfig<Poll> {
    @Override
    public Class<Poll> getEntityClass() {
        return Poll.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(PollQueryConfig.PollQueryParamEnum.values());
    }

    public enum PollQueryParamEnum implements QueryParamEnum {

        ID("id"),
        COOPERATION_ID("cooperation.id"),
        CREATION_DATE("creationDate"),
        COMPLETION_DATE("completionDate"),
        STATUS("status"),
        TYPE("type");

        private final String parameter;

        PollQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }

    }
}
