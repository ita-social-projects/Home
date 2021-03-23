package com.softserveinc.ita.homeproject.application.config.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.UserQueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.UserQueryConfig.UserQueryParamEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRSQLEndpointConfig implements RSQLEndpointConfig<User, UserQueryParamEnum> {

    @Autowired
    private UserQueryConfig queryConfig;

    @Override
    public Map<UserQueryParamEnum, String> getMappings() {
        EnumMap<UserQueryParamEnum, String> map = new EnumMap<>(UserQueryParamEnum.class);

        map.put(UserQueryParamEnum.EMAIL, "email");
        map.put(UserQueryParamEnum.FIRST_NAME, "firstName");
        map.put(UserQueryParamEnum.LAST_NAME, "lastName");
        map.put(UserQueryParamEnum.CONTACT_PHONE, "contacts.phone");
        map.put(UserQueryParamEnum.CONTACT_EMAIL, "contacts.email");
        return map;
    }

    @Override
    public QueryConfig<User> getQueryConfig() {
        return queryConfig;
    }

}
