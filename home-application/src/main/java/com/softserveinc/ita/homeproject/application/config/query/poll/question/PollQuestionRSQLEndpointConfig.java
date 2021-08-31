package com.softserveinc.ita.homeproject.application.config.query.poll.question;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.base.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.base.RSQLEndpointConfig;
import com.softserveinc.ita.homeproject.homedata.poll.entity.question.PollQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PollQuestionRSQLEndpointConfig implements
        RSQLEndpointConfig<PollQuestion, PollQuestionQueryConfig.PollQuestionQueryParamEnum> {

    @Autowired
    private PollQuestionQueryConfig queryConfig;

    @Override
    public Map<PollQuestionQueryConfig.PollQuestionQueryParamEnum, String> getMappings() {
        EnumMap<PollQuestionQueryConfig.PollQuestionQueryParamEnum, String> map =
                new EnumMap<>(PollQuestionQueryConfig.PollQuestionQueryParamEnum.class);

        map.put(PollQuestionQueryConfig.PollQuestionQueryParamEnum.POLL_ID, "poll_id");
        map.put(PollQuestionQueryConfig.PollQuestionQueryParamEnum.TYPE, "type");

        return map;
    }

    @Override
    public QueryConfig<PollQuestion> getQueryConfig() {
        return queryConfig;
    }
}
