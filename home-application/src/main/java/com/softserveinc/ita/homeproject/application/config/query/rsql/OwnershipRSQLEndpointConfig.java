package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.OwnershipQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.Ownership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnershipRSQLEndpointConfig implements
        RSQLEndpointConfig<Ownership, OwnershipQueryConfig.OwnershipQueryParamEnum> {

    @Autowired
    private OwnershipQueryConfig queryConfig;

    @Override
    public Map<OwnershipQueryConfig.OwnershipQueryParamEnum, String> getMappings() {
        EnumMap<OwnershipQueryConfig.OwnershipQueryParamEnum, String> map =
                new EnumMap<>(OwnershipQueryConfig.OwnershipQueryParamEnum.class);

        map.put(OwnershipQueryConfig.OwnershipQueryParamEnum.APARTMENT_ID, "apartment_id");
        map.put(OwnershipQueryConfig.OwnershipQueryParamEnum.USER_ID, "user_id");
        map.put(OwnershipQueryConfig.OwnershipQueryParamEnum.OWNERSHIP_PART, "ownership_part");

        return map;
    }

    @Override
    public QueryConfig<Ownership> getQueryConfig() {
        return queryConfig;
    }
}
