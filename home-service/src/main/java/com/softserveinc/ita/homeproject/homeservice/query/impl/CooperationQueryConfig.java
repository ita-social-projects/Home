package com.softserveinc.ita.homeproject.homeservice.query.impl;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;
import org.springframework.stereotype.Component;


@Component
public class CooperationQueryConfig implements QueryConfig<Cooperation> {

    @Override
    public Class<Cooperation> getEntityClass() {
        return Cooperation.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(CooperationQueryParamEnum.values());
    }

    public enum CooperationQueryParamEnum implements QueryParamEnum {

        ID("id"),
        NAME("name"),
        IBAN("iban"),
        USREO("usreo");

        private final String parameter;

        CooperationQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
    }
}
