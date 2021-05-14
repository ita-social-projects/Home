package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.ApartmentQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApartmentRSQLEndpointConfig implements
        RSQLEndpointConfig<Apartment, ApartmentQueryConfig.ApartmentQueryParamEnum> {

    @Autowired
    private ApartmentQueryConfig queryConfig;

    @Override
    public Map<ApartmentQueryConfig.ApartmentQueryParamEnum, String> getMappings() {
        EnumMap<ApartmentQueryConfig.ApartmentQueryParamEnum, String> map =
                new EnumMap<>(ApartmentQueryConfig.ApartmentQueryParamEnum.class);

        map.put(ApartmentQueryConfig.ApartmentQueryParamEnum.HOUSE_ID, "house_id");
        map.put(ApartmentQueryConfig.ApartmentQueryParamEnum.APARTMENT_NUMBER, "apartment_number");
        map.put(ApartmentQueryConfig.ApartmentQueryParamEnum.APARTMENT_AREA, "apartment_area");

        return map;
    }

    @Override
    public QueryConfig<Apartment> getQueryConfig() {
        return queryConfig;
    }
}
