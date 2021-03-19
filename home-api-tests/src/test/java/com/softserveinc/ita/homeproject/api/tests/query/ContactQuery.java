package com.softserveinc.ita.homeproject.api.tests.query;

import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.ContactApi;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadUser;

public class ContactQuery extends BaseQuery{

    private Long userId;

    private String contactId;

    private String phone;

    private String email;

    private String main;

    private String type;

    private ContactApi contactApi;

    private UserApi userApi;

    private ReadUser readUser;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setContactApi(ContactApi contactApi) {
        this.contactApi = contactApi;
    }

    public void setUserApi(UserApi userApi) {
        this.userApi = userApi;
    }

    public void setReadUser(ReadUser readUser) {
        this.readUser = readUser;
    }

    public List<ReadContact> perform() throws ApiException {
        return contactApi
            .queryContactsOnUser(this.userId, this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                contactId,
                phone,
                email,
                main,
                type);
    }

    public static class Builder extends BaseBuilder<ContactQuery, ContactQuery.Builder> {

        public Builder(ContactApi contactApi) {
            queryClass.setContactApi(contactApi);
        }


        public Builder userId(Long userId) {
            queryClass.setUserId(userId);
            return this;
        }

        public Builder contactId(String contactId) {
            queryClass.setContactId(contactId);
            return this;
        }

        public Builder phone(String phone) {
            queryClass.setPhone(phone);
            return this;
        }

        public Builder email(String email) {
            queryClass.setEmail(email);
            return this;
        }

        public Builder main(String main) {
            queryClass.setMain(main);
            return this;
        }

        public Builder type(String type) {
            queryClass.setType(type);
            return this;
        }

        public Builder readUser(ReadUser readUser) {
            queryClass.setReadUser(readUser);
            return this;
        }

        public Builder userApi(UserApi userApi) {
            queryClass.setUserApi(userApi);
            return this;
        }

        @Override
        protected ContactQuery getActual() {
            return new ContactQuery();
        }

        @Override
        protected ContactQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
