package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.PollQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PollRSQLEndpointConfig implements
    RSQLEndpointConfig<Poll, PollQueryConfig.PollQueryParamEnum> {


    @Autowired
    private PollQueryConfig queryConfig;

    @Override
    public Map<PollQueryConfig.PollQueryParamEnum, String> getMappings() {
        EnumMap<PollQueryConfig.PollQueryParamEnum, String> map =
            new EnumMap<>(PollQueryConfig.PollQueryParamEnum.class);

        map.put(PollQueryConfig.PollQueryParamEnum.COOPERATION_ID, "cooperation_id");
        map.put(PollQueryConfig.PollQueryParamEnum.CREATION_DATE, "creation_date");
        map.put(PollQueryConfig.PollQueryParamEnum.COMPLETION_DATE, "completion_date");
        map.put(PollQueryConfig.PollQueryParamEnum.STATUS, "status");
        map.put(PollQueryConfig.PollQueryParamEnum.TYPE, "type");

        return map;
    }

    @Override
    public QueryConfig<Poll> getQueryConfig() {
        return queryConfig;
    }
}
