package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.PollHouseQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.PollHouseQueryConfig.PollHouseQueryParamEnum;
import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.house.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PollHouseRSQLEndpointConfig implements RSQLEndpointConfig<House, PollHouseQueryParamEnum> {
    @Autowired
    private PollHouseQueryConfig pollHouseQueryConfig;

    @Override
    public Map<PollHouseQueryParamEnum, String> getMappings() {
        EnumMap<PollHouseQueryParamEnum, String> map = new EnumMap<>(PollHouseQueryParamEnum.class);

        map.put(PollHouseQueryParamEnum.POLL_ID, "poll_id");
        map.put(PollHouseQueryParamEnum.QUANTITY_FLAT, "quantity_flat");
        map.put(PollHouseQueryParamEnum.ADJOINING_AREA, "adjoining_area");
        map.put(PollHouseQueryParamEnum.HOUSE_AREA, "house_area");

        return map;
    }

    @Override
    public QueryConfig<House> getQueryConfig() {
        return pollHouseQueryConfig;
    }
}
