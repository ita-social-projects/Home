package com.softserveinc.ita.homeproject.application.config.query;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Ownership;
import org.springframework.stereotype.Component;

@Component
public class OwnershipQueryConfig implements QueryConfig<Ownership> {

    @Override
    public Class<Ownership> getEntityClass() {
        return Ownership.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(OwnershipQueryConfig.OwnershipQueryParamEnum.values());
    }

    public enum OwnershipQueryParamEnum implements QueryParamEnum {

        ID("id"),
        APARTMENT_ID("apartment.id"),
        USER_ID("user.id"),
        OWNERSHIP_PART("ownershipPart");

        private final String parameter;

        OwnershipQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }

    }
}
