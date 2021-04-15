package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.HouseQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.HouseQueryConfig.HouseQueryParamEnum;
import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HouseRSQLEndpointConfig implements RSQLEndpointConfig<House, HouseQueryConfig.HouseQueryParamEnum> {

    @Autowired
    private HouseQueryConfig queryConfig;

    @Override
    public Map<HouseQueryParamEnum, String> getMappings() {
        EnumMap<HouseQueryParamEnum, String> map = new EnumMap<>(HouseQueryParamEnum.class);

        map.put(HouseQueryParamEnum.COOPERATION_ID, "cooperation_id");
        map.put(HouseQueryParamEnum.QUANTITY_FLAT, "quantity_flat");
        map.put(HouseQueryParamEnum.ADJOINING_AREA, "adjoining_area");
        map.put(HouseQueryParamEnum.HOUSE_AREA, "house_area");

        return map;
    }

    @Override
    public QueryConfig<House> getQueryConfig() {
        return queryConfig;
    }
}
