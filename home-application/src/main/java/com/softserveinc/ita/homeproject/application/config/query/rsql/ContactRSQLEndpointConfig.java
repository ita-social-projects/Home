package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.ContactQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.ContactQueryConfig.ContactQueryParamEnum;
import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactRSQLEndpointConfig implements RSQLEndpointConfig<Contact, ContactQueryParamEnum> {

    @Autowired
    private ContactQueryConfig queryConfig;

    @Override
    public Map<ContactQueryParamEnum, String> getMappings() {
        EnumMap<ContactQueryParamEnum, String> map = new EnumMap<>(ContactQueryParamEnum.class);

        map.put(ContactQueryParamEnum.USER_ID, "user_id");
        map.put(ContactQueryParamEnum.PHONE, "phone");
        map.put(ContactQueryParamEnum.EMAIL, "email");
        map.put(ContactQueryParamEnum.MAIN, "main");
        map.put(ContactQueryParamEnum.TYPE, "type");
        return map;
    }

    @Override
    public QueryConfig<Contact> getQueryConfig() {
        return queryConfig;
    }
}
