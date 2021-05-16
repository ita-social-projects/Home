package com.softserveinc.ita.homeproject.api.tests.query;

import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.CooperationContactApi;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadCooperation;

public class CooperationContactQuery extends BaseQuery{

    private Long cooperationId;

    private Long id;

    private String phone;

    private String email;

    private String main;

    private ContactType type;

    private CooperationContactApi contactApi;

    private CooperationApi cooperationApi;

    private ReadCooperation readCooperation;

    public void setCooperationId(Long cooperationId) {
        this.cooperationId = cooperationId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setType(ContactType type) {
        this.type = type;
    }

    public void setContactApi(CooperationContactApi contactApi) {
        this.contactApi = contactApi;
    }

    public void setCooperationApi(CooperationApi cooperationApi) {
        this.cooperationApi = cooperationApi;
    }

    public void setReadCooperation(ReadCooperation readCooperation) {
        this.readCooperation = readCooperation;
    }

    public List<ReadContact> perform() throws ApiException {
        return contactApi
            .queryContactsOnCooperation(this.cooperationId, this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                id,
                phone,
                email,
                main,
                type);
    }

    public static class Builder extends BaseBuilder<CooperationContactQuery, CooperationContactQuery.Builder> {

        public Builder(CooperationContactApi contactApi) {
            queryClass.setContactApi(contactApi);
        }

        public Builder cooperationId(Long userId) {
            queryClass.setCooperationId(userId);
            return this;
        }

        public Builder id(Long id) {
            queryClass.setId(id);
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

        public Builder type(ContactType type) {
            queryClass.setType(type);
            return this;
        }

        public Builder readCooperation(ReadCooperation readCooperation) {
            queryClass.setReadCooperation(readCooperation);
            return this;
        }

        public Builder cooperationApi(CooperationApi cooperationApi) {
            queryClass.setCooperationApi(cooperationApi);
            return this;
        }

        @Override
        protected CooperationContactQuery getActual() {
            return new CooperationContactQuery();
        }

        @Override
        protected CooperationContactQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
