package com.softserveinc.ita.homeproject.application.config.query.user;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.user.User;
import org.springframework.stereotype.Component;

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
        MIDDLE_NAME("middleName"),
        LAST_NAME("lastName"),
        CONTACT_PHONE("contacts.phone"),
        CONTACT_EMAIL("contacts.email");

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
