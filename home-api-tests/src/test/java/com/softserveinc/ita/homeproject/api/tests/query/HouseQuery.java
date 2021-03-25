package com.softserveinc.ita.homeproject.api.tests.query;

import java.math.BigDecimal;
import java.util.List;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.HouseApi;
import com.softserveinc.ita.homeproject.model.ReadHouse;

public class HouseQuery extends BaseQuery {

    private Long houseId;

    private Integer quantityFlat;

    private Integer adjoiningArea;

    private BigDecimal houseArea;

    private HouseApi houseApi;

    private ReadHouse readHouse;

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
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
            .queryHouse(readHouse.getId(), this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                houseId,
                quantityFlat,
                adjoiningArea,
                houseArea);
    }

    public static class Builder extends BaseBuilder<HouseQuery, HouseQuery.Builder>{

        public Builder(HouseApi houseApi){
            queryClass.setHouseApi(houseApi);
        }

        public Builder houseId(Long houseId){
            queryClass.setHouseId(houseId);
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
