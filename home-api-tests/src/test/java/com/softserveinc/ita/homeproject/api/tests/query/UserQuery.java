package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.model.ReadUser;

import java.util.List;

public class UserQuery extends BaseQuery {
    private String email;
    private String firstName;
    private String lastName;
    private String contact;
    private UserApi userApi;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUserApi(UserApi userApi) {
        this.userApi = userApi;
    }

    public List<ReadUser> perfom() throws ApiException {
        return userApi
                .getAllUsers(this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        this.getId(),
                        email,
                        firstName,
                        lastName,
                        contact);
    }

    public static class Builder extends BaseBuilder<UserQuery, Builder> {

        public Builder(UserApi userApi) {
           queryClass.setUserApi(userApi);
        }

        public Builder email(String email) {
            queryClass.setEmail(email);
            return this;
        }

        public Builder firstName(String firstName) {
            queryClass.setFirstName(firstName);
            return this;
        }

        public Builder lastName(String lastName) {
            queryClass.setLastName(lastName);
            return this;
        }

        public Builder contact(String contact) {
            queryClass.setContact(contact);
            return this;
        }

        @Override
        protected UserQuery getActual() {
            return new UserQuery();
        }

        @Override
        protected Builder getActualBuilder() {
            return this;
        }
    }
}
