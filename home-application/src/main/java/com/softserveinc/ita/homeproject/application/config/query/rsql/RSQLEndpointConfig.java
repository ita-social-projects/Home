package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;

public interface RSQLEndpointConfig<T extends BaseEntity, Q extends QueryParamEnum> {

    Map<Q, String> getMappings();

    QueryConfig<T> getQueryConfig();
}
