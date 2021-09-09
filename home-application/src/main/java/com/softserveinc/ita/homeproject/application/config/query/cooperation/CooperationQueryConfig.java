package com.softserveinc.ita.homeproject.application.config.query.cooperation;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
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
