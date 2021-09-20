package com.softserveinc.ita.homeproject.application.config.query.user;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.RSQLEndpointConfig;
import com.softserveinc.ita.homeproject.application.config.query.user.UserQueryConfig.UserQueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.user.User;
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
        map.put(UserQueryParamEnum.FIRST_NAME, "first_name");
        map.put(UserQueryParamEnum.LAST_NAME, "last_name");
        map.put(UserQueryParamEnum.CONTACT_PHONE, "contacts_phone");
        map.put(UserQueryParamEnum.CONTACT_EMAIL, "contacts_email");
        return map;
    }

    @Override
    public QueryConfig<User> getQueryConfig() {
        return queryConfig;
    }

}
