package com.softserveinc.ita.homeproject.application.config.query.cooperation;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.RSQLEndpointConfig;
import com.softserveinc.ita.homeproject.application.config.query.cooperation.CooperationQueryConfig.CooperationQueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CooperationRSQLEndpointConfig implements RSQLEndpointConfig
        <Cooperation, CooperationQueryParamEnum> {

    @Autowired
    private CooperationQueryConfig queryConfig;

    @Override
    public Map<CooperationQueryConfig.CooperationQueryParamEnum, String> getMappings() {
        EnumMap<CooperationQueryParamEnum, String> map = new EnumMap<>(CooperationQueryParamEnum.class);

        map.put(CooperationQueryParamEnum.ID, "id");
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
