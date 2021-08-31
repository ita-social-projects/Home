package com.softserveinc.ita.homeproject.application.config.query.general.contact.cooperation;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.base.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.base.RSQLEndpointConfig;
import com.softserveinc.ita.homeproject.application.config.query.general.contact.cooperation.CoopContactQueryConfig.CoopContactQueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.general.entity.contact.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoopContactRSQLEndpointConfig implements RSQLEndpointConfig<Contact, CoopContactQueryParamEnum> {

    @Autowired
    private CoopContactQueryConfig cooperationQueryConfig;

    @Override
    public Map<CoopContactQueryParamEnum, String> getMappings() {
        EnumMap<CoopContactQueryParamEnum, String> map = new EnumMap<>(CoopContactQueryParamEnum.class);

        map.put(CoopContactQueryParamEnum.COOPERATION_ID, "cooperation_id");
        map.put(CoopContactQueryParamEnum.PHONE, "phone");
        map.put(CoopContactQueryParamEnum.EMAIL, "email");
        map.put(CoopContactQueryParamEnum.MAIN, "main");
        map.put(CoopContactQueryParamEnum.TYPE, "type");
        return map;
    }

    @Override
    public QueryConfig<Contact> getQueryConfig() {
        return cooperationQueryConfig;
    }
}
