package com.softserveinc.ita.homeproject.application.config.query;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactQueryConfig implements QueryConfig<Contact> {

    @Override
    public Class<Contact> getEntityClass() {
        return Contact.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(ContactQueryParamEnum.values());
    }

    public enum ContactQueryParamEnum implements QueryParamEnum {

        ID("id"),
        USER_ID("user.id"),
        PHONE("phone"),
        EMAIL("email"),
        MAIN("main"),
        TYPE("type");

        private final String parameter;

        ContactQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
    }
}
