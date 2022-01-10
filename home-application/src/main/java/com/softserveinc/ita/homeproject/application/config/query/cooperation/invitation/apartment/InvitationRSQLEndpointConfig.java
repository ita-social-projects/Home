package com.softserveinc.ita.homeproject.application.config.query.cooperation.invitation.apartment;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.RSQLEndpointConfig;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvitationRSQLEndpointConfig implements
        RSQLEndpointConfig<Invitation, InvitationQueryConfig.InvitationQueryParamEnum> {

    @Autowired
    private InvitationQueryConfig queryConfig;

    @Override
    public Map<InvitationQueryConfig.InvitationQueryParamEnum, String> getMappings() {
        EnumMap<InvitationQueryConfig.InvitationQueryParamEnum, String> map =
                new EnumMap<>(InvitationQueryConfig.InvitationQueryParamEnum.class);

        map.put(InvitationQueryConfig.InvitationQueryParamEnum.APARTMENT_ID, "apartment_id");
        map.put(InvitationQueryConfig.InvitationQueryParamEnum.COOPERATION_ID, "cooperation_id");
        map.put(InvitationQueryConfig.InvitationQueryParamEnum.EMAIL, "email");
        map.put(InvitationQueryConfig.InvitationQueryParamEnum.STATUS, "status");
        map.put(InvitationQueryConfig.InvitationQueryParamEnum.TYPE, "type");

        return map;
    }

    @Override
    public QueryConfig<Invitation> getQueryConfig() {
        return queryConfig;
    }
}
