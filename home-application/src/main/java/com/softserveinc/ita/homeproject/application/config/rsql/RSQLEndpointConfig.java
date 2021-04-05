package com.softserveinc.ita.homeproject.application.config.rsql;

import java.util.Map;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.application.service.QueryConfig;
import com.softserveinc.ita.homeproject.application.service.QueryParamEnum;

public interface RSQLEndpointConfig<T extends BaseEntity, Q extends QueryParamEnum> {

    Map<Q, String> getMappings();

    QueryConfig<T> getQueryConfig();
}
