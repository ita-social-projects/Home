package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.ApartmentApi;
import com.softserveinc.ita.homeproject.api.ApartmentOwnershipApi;
import com.softserveinc.ita.homeproject.model.ReadApartment;
import com.softserveinc.ita.homeproject.model.ReadOwnership;

import java.math.BigDecimal;
import java.util.List;

public class OwnershipQuery extends BaseQuery {
    private Long apartmentId;

    private BigDecimal ownershipPart;

    private ApartmentOwnershipApi ownershipApi;

    private Long id;

    private Long userId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnershipApi(ApartmentOwnershipApi ownershipApi) {
        this.ownershipApi = ownershipApi;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setOwnershipPart(BigDecimal ownershipPart) {
        this.ownershipPart = ownershipPart;
    }

    public List<ReadOwnership> perform() throws ApiException {
        return ownershipApi
                .queryOwnership(apartmentId, this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        id,
                        userId,
                        ownershipPart);
    }

    public static class Builder extends BaseBuilder<OwnershipQuery, OwnershipQuery.Builder> {

        public Builder(ApartmentOwnershipApi ownershipApi) {
            queryClass.setOwnershipApi(ownershipApi);
        }

        public OwnershipQuery.Builder apartmentId(Long apartmentId) {
            queryClass.setApartmentId(apartmentId);
            return this;
        }

        public OwnershipQuery.Builder userId(Long userId) {
            queryClass.setUserId(userId);
            return this;
        }

        public OwnershipQuery.Builder ownershipPart(BigDecimal ownershipPart) {
            queryClass.setOwnershipPart(ownershipPart);
            return this;
        }

        @Override
        protected OwnershipQuery getActual() {
            return new OwnershipQuery();
        }

        @Override
        protected OwnershipQuery.Builder getActualBuilder() {
            return this;
        }
    }
}
