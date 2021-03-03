package com.softserveinc.ita.homeproject.application.config.rsql;

import java.util.HashMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.ContactQueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.ContactQueryConfig.ContactQueryParamEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactsRSQLEndpointConfig implements RSQLEndpointConfig<Contact, ContactQueryParamEnum> {

    @Autowired
    private ContactQueryConfig queryConfig;

    @Override
    public Map<ContactQueryParamEnum, String> getMappings() {
        HashMap<ContactQueryParamEnum, String> map = new HashMap<>();

        map.put(ContactQueryParamEnum.USER_ID, "user.id");
        map.put(ContactQueryParamEnum.PHONE, "phone");
        map.put(ContactQueryParamEnum.EMAIL, "email");
        map.put(ContactQueryParamEnum.MAIN, "main");
        map.put(ContactQueryParamEnum.TYPE, "contactType");


        return map;
    }

    @Override
    public QueryConfig<Contact> getQueryConfig() {
        return queryConfig;
    }
}
