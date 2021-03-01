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
        map.put(ContactQueryParamEnum.CONTACT_PHONE, "contactPhone");
        map.put(ContactQueryParamEnum.CONTACT_EMAIL, "contactEmail");
        map.put(ContactQueryParamEnum.PRIMARY, "primary");


        return map;
    }

    @Override
    public QueryConfig<Contact> getQueryConfig() {
        return queryConfig;
    }
}
