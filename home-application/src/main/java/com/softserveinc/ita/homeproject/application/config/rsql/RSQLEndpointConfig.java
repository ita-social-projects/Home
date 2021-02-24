package com.softserveinc.ita.homeproject.application.config.rsql;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;

import java.util.Map;

public interface RSQLEndpointConfig<T extends BaseEntity, Q extends QueryParamEnum> {

    Map<Q, String> getMappings();
    QueryConfig<T> getQueryConfig();
}
