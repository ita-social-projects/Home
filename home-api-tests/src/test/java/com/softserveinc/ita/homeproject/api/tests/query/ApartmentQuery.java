package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.ApartmentApi;
import com.softserveinc.ita.homeproject.model.ReadApartment;

import java.math.BigDecimal;
import java.util.List;

public class ApartmentQuery extends BaseQuery {

    private Long houseId;

    private String apartmentNumber;

    private BigDecimal apartmentArea;

    private ApartmentApi apartmentApi;

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public void setApartmentApi(ApartmentApi apartmentApi) {
        this.apartmentApi = apartmentApi;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setApartmentArea(BigDecimal apartmentArea) {
        this.apartmentArea = apartmentArea;
    }

    public List<ReadApartment> perform() throws ApiException {
        return apartmentApi
                .queryApartment(houseId, this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        id,
                        apartmentNumber,
                        apartmentArea);
    }

    public static class Builder extends BaseBuilder<ApartmentQuery, ApartmentQuery.Builder> {

        public Builder(ApartmentApi apartmentApi) {
            queryClass.setApartmentApi(apartmentApi);
        }


        public ApartmentQuery.Builder houseId(Long houseId) {
            queryClass.setHouseId(houseId);
            return this;
        }

        public ApartmentQuery.Builder apartmentNumber(String number) {
            queryClass.setApartmentNumber(number);
            return this;
        }

        public ApartmentQuery.Builder apartmentArea(BigDecimal area) {
            queryClass.setApartmentArea(area);
            return this;
        }

        @Override
        protected ApartmentQuery getActual() {
            return new ApartmentQuery();
        }

        @Override
        protected ApartmentQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
