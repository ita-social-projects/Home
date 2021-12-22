package com.softserveinc.ita.homeproject.application.config.query.cooperation.invitation.apartment;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import org.springframework.stereotype.Component;

@Component
public class InvitationQueryConfig implements QueryConfig<Invitation> {
    @Override
    public Class<Invitation> getEntityClass() {
        return Invitation.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(InvitationQueryParamEnum.values());
    }

    public enum InvitationQueryParamEnum implements QueryParamEnum {

        ID("id"),
        APARTMENT_ID("apartment.id"),
        COOPERATION_ID("cooperationId"),
        EMAIL("email"),
        STATUS("status"),
        TYPE("type");


        private final String parameter;

        InvitationQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
    }
}
