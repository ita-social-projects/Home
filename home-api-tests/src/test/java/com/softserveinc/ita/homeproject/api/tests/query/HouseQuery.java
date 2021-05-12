package com.softserveinc.ita.homeproject.api.tests.query;

import java.math.BigDecimal;
import java.util.List;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.HouseApi;
import com.softserveinc.ita.homeproject.model.ReadHouse;

public class HouseQuery extends BaseQuery {

    private Long cooperationId;

    private Long id;

    private Integer quantityFlat;

    private Integer adjoiningArea;

    private BigDecimal houseArea;

    private HouseApi houseApi;

    private ReadHouse readHouse;

    public void setCooperationId(Long cooperationId) {
        this.cooperationId = cooperationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantityFlat(Integer quantityFlat) {
        this.quantityFlat = quantityFlat;
    }

    public void setAdjoiningArea(Integer adjoiningArea) {
        this.adjoiningArea = adjoiningArea;
    }

    public void setHouseArea(BigDecimal houseArea) {
        this.houseArea = houseArea;
    }

    public void setHouseApi(HouseApi houseApi) {
        this.houseApi = houseApi;
    }

    public void setReadHouse(ReadHouse readHouse) {
        this.readHouse = readHouse;
    }

    public List<ReadHouse> perform() throws ApiException {
        return houseApi
            .queryHouse(cooperationId, this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                id,
                quantityFlat,
                adjoiningArea,
                houseArea);
    }

    public static class Builder extends BaseBuilder<HouseQuery, HouseQuery.Builder>{

        public Builder(HouseApi houseApi){
            queryClass.setHouseApi(houseApi);
        }


        public Builder cooperationId(Long cooperationId){
            queryClass.setCooperationId(cooperationId);
            return this;
        }

        public Builder id(Long id){
            queryClass.setId(id);
            return this;
        }

        public Builder quantityFlat(Integer quantityFlat){
            queryClass.setQuantityFlat(quantityFlat);
            return this;
        }

        public Builder adjoiningArea(Integer adjoiningArea){
            queryClass.setAdjoiningArea(adjoiningArea);
            return this;
        }

        public Builder houseArea(BigDecimal houseArea){
            queryClass.setHouseArea(houseArea);
            return this;
        }

        public Builder readHouse(ReadHouse readHouse){
            queryClass.setReadHouse(readHouse);
            return this;
        }

        @Override
        protected HouseQuery getActual() {
            return new HouseQuery();
        }

        @Override
        protected Builder getActualBuilder() {
            return this;
        }
    }
}
