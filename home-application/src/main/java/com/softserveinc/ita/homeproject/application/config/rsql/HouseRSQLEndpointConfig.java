package com.softserveinc.ita.homeproject.application.config.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.HouseQueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.HouseQueryConfig.HouseQueryParamEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HouseRSQLEndpointConfig implements RSQLEndpointConfig<House, HouseQueryParamEnum> {

    @Autowired
    private HouseQueryConfig queryConfig;

    @Override
    public Map<HouseQueryParamEnum, String> getMappings() {
        EnumMap<HouseQueryParamEnum, String> map = new EnumMap<>(HouseQueryParamEnum.class);

        map.put(HouseQueryParamEnum.COOPERATION_ID, "cooperation_id");
        map.put(HouseQueryParamEnum.QUANTITY_FLAT, "quantityFlat");
        map.put(HouseQueryParamEnum.ADJOINING_AREA, "adjoiningArea");
        map.put(HouseQueryParamEnum.HOUSE_AREA, "houseArea");

        return map;
    }

    @Override
    public QueryConfig<House> getQueryConfig() {
        return queryConfig;
    }
}
