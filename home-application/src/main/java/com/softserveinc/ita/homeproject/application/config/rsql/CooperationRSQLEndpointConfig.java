package com.softserveinc.ita.homeproject.application.config.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.CooperationQueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.CooperationQueryConfig.CooperationQueryParamEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CooperationRSQLEndpointConfig implements RSQLEndpointConfig
    <Cooperation, CooperationQueryConfig.CooperationQueryParamEnum> {

    @Autowired
    private CooperationQueryConfig queryConfig;

    @Override
    public Map<CooperationQueryParamEnum, String> getMappings() {
        EnumMap<CooperationQueryParamEnum, String> map = new EnumMap<>(CooperationQueryParamEnum.class);

        map.put(CooperationQueryParamEnum.NAME, "name");
        map.put(CooperationQueryParamEnum.IBAN, "iban");
        map.put(CooperationQueryParamEnum.USREO, "usreo");

        return map;
    }

    @Override
    public QueryConfig<Cooperation> getQueryConfig() {
        return queryConfig;
    }
}
