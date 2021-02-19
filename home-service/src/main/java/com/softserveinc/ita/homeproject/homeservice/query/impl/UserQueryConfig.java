package com.softserveinc.ita.homeproject.homeservice.query.impl;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserQueryConfig implements QueryConfig<User> {

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(UserQueryParamEnum.values());
    }

    public enum UserQueryParamEnum implements QueryParamEnum {

        ID("id"),
        EMAIL("email"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        CONTACT_PHONE("contactPhone"),
        CONTACT_EMAIL("contactEmail");

        private final String parameter;

        UserQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
    }
}
