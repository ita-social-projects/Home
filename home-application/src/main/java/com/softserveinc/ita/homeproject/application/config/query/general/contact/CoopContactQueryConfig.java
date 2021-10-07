package com.softserveinc.ita.homeproject.application.config.query.general.contact;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import org.springframework.stereotype.Component;

@Component
public class CoopContactQueryConfig implements QueryConfig<Contact> {

    @Override
    public Class<Contact> getEntityClass() {
        return Contact.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(CoopContactQueryConfig.CoopContactQueryParamEnum.values());
    }

    public enum CoopContactQueryParamEnum implements QueryParamEnum {

        ID("id"),
        COOPERATION_ID("cooperation.id"),
        PHONE("phone"),
        EMAIL("email"),
        MAIN("main"),
        TYPE("type");

        private final String parameter;

        CoopContactQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
    }
}
