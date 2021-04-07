package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.UserQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.UserQueryConfig.UserQueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.entity.User;
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
