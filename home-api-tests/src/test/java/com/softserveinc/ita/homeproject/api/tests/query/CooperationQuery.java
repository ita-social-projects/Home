package com.softserveinc.ita.homeproject.api.tests.query;

import java.util.List;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.model.ReadCooperation;

public class CooperationQuery extends BaseQuery {

    private Long id;

    private String name;

    private String iban;

    private String usreo;

    private CooperationApi cooperationApi;

    private ReadCooperation readCooperation;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setUsreo(String usreo) {
        this.usreo = usreo;
    }

    public void setCooperationApi(CooperationApi cooperationApi) {
        this.cooperationApi = cooperationApi;
    }

    public void setReadCooperation(ReadCooperation readCooperation) {
        this.readCooperation = readCooperation;
    }

    public List<ReadCooperation> perform() throws ApiException {
        return cooperationApi
            .queryCooperation(this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                id,
                name,
                iban,
                usreo);
    }

    public static class Builder extends BaseBuilder<CooperationQuery, CooperationQuery.Builder> {

        public Builder(CooperationApi cooperationApi){
            queryClass.setCooperationApi(cooperationApi);
        }

        public Builder id(Long id){
            queryClass.setId(id);
            return this;
        }

        public Builder name(String name){
            queryClass.setName(name);
            return this;
        }

        public Builder iban(String iban){
            queryClass.setIban(iban);
            return this;
        }

        public Builder usreo(String usreo){
            queryClass.setUsreo(usreo);
            return this;
        }

        public Builder readCooperation(ReadCooperation readCooperation){
            queryClass.setReadCooperation(readCooperation);
            return this;
        }

        @Override
        protected CooperationQuery getActual() {
            return new CooperationQuery();
        }

        @Override
        protected Builder getActualBuilder() {
            return this;
        }
    }
}
