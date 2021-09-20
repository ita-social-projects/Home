package com.softserveinc.ita.homeproject.application.config.query.cooperation.invitation.apartment;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import org.springframework.stereotype.Component;

@Component
public class ApartmentInvitationQueryConfig implements QueryConfig<ApartmentInvitation> {

    @Override
    public Class<ApartmentInvitation> getEntityClass() {
        return ApartmentInvitation.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(ApartmentInvitationQueryParamEnum.values());
    }

    public enum ApartmentInvitationQueryParamEnum implements QueryParamEnum {

        ID("id"),
        APARTMENT_ID("apartment.id"),
        EMAIL("email"),
        OWNERSHIP_PART("ownershipPart"),
        STATUS("status");

        private final String parameter;

        ApartmentInvitationQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }

    }
}
