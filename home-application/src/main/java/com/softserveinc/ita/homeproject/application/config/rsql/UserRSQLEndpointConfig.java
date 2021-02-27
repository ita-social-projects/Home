package com.softserveinc.ita.homeproject.application.config.rsql;

import java.util.HashMap;
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
        HashMap<UserQueryParamEnum, String> map = new HashMap<>();

        map.put(UserQueryParamEnum.EMAIL, "email");
        map.put(UserQueryParamEnum.FIRST_NAME, "firstName");
        map.put(UserQueryParamEnum.LAST_NAME, "lastName");
        map.put(UserQueryParamEnum.CONTACT_PHONE, "contacts.contactPhone");
        map.put(UserQueryParamEnum.CONTACT_EMAIL, "contacts.contactEmail");


        return map;
    }

    @Override
    public QueryConfig<User> getQueryConfig() {
        return queryConfig;
    }

}
