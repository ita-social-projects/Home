package com.softserveinc.ita.homeproject.api.tests.query;

import java.math.BigDecimal;
import java.util.List;

import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.PolledHouseApi;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HousePollQuery extends BaseQuery {
    private Long pollId;

    private Integer quantityFlat;

    private Integer adjoiningArea;

    private BigDecimal houseArea;

    private PolledHouseApi polledHouseApi;

    public List<ReadHouse> perform() throws ApiException {
        return polledHouseApi
            .queryPolledHouse(pollId,
                this.getPageNumber(),
                this.getPageSize(),
                this.getSort(),
                this.getFilter(),
                this.getId(),
                this.getQuantityFlat(),
                this.getAdjoiningArea(),
                this.getHouseArea());
    }

    public static class Builder extends BaseBuilder<HousePollQuery, HousePollQuery.Builder> {

        public Builder(PolledHouseApi polledHouseApi) {
            queryClass.setPolledHouseApi(polledHouseApi);
        }

        public HousePollQuery.Builder pollId(Long pollId) {
            queryClass.setPollId(pollId);
            return this;
        }

        public HousePollQuery.Builder quantityFlat(Integer quantityFlat) {
            queryClass.setQuantityFlat(quantityFlat);
            return this;
        }

        public HousePollQuery.Builder adjoiningArea(Integer adjoiningArea) {
            queryClass.setAdjoiningArea(adjoiningArea);
            return this;
        }

        public HousePollQuery.Builder houseArea(BigDecimal houseArea) {
            queryClass.setHouseArea(houseArea);
            return this;
        }

        @Override
        protected HousePollQuery getActual() {
            return new HousePollQuery();
        }

        @Override
        protected Builder getActualBuilder() {
            return this;
        }
    }

}
