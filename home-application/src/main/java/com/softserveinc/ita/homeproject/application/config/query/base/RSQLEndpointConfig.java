package com.softserveinc.ita.homeproject.application.config.query.base;

import java.util.Map;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;

public interface RSQLEndpointConfig<T extends BaseEntity, Q extends QueryParamEnum> {

    Map<Q, String> getMappings();

    QueryConfig<T> getQueryConfig();
}
