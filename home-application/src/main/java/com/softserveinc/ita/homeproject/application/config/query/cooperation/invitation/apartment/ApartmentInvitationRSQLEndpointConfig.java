package com.softserveinc.ita.homeproject.application.config.query.cooperation.invitation.apartment;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.RSQLEndpointConfig;
import com.softserveinc.ita.homeproject.application.config.query.cooperation.invitation.apartment.ApartmentInvitationQueryConfig.ApartmentInvitationQueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApartmentInvitationRSQLEndpointConfig implements
        RSQLEndpointConfig<ApartmentInvitation, ApartmentInvitationQueryParamEnum> {

    @Autowired
    private ApartmentInvitationQueryConfig queryConfig;

    @Override
    public Map<ApartmentInvitationQueryParamEnum, String> getMappings() {
        EnumMap<ApartmentInvitationQueryParamEnum, String> map =
                new EnumMap<>(ApartmentInvitationQueryParamEnum.class);

        map.put(ApartmentInvitationQueryParamEnum.APARTMENT_ID, "apartment_id");
        map.put(ApartmentInvitationQueryParamEnum.EMAIL, "email");
        map.put(ApartmentInvitationQueryParamEnum.OWNERSHIP_PART, "ownership_part");
        map.put(ApartmentInvitationQueryParamEnum.STATUS, "status");

        return map;
    }

    @Override
    public QueryConfig<ApartmentInvitation> getQueryConfig() {
        return queryConfig;
    }
}
