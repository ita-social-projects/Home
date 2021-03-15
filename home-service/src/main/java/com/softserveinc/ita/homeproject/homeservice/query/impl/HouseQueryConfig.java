package com.softserveinc.ita.homeproject.homeservice.query.impl;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;


import java.util.List;

public class HouseQueryConfig implements QueryConfig<House> {
    @Override
    public Class<House> getEntityClass() {
        return House.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return null;
    }

    public enum HouseQueryParamEnum implements QueryParamEnum {

        ID("id"),
        QUANTITY_FLAT("quantityFlat"),
        ADJOINING_AREA("adjoiningArea"),
        HOUSE_AREA("houseArea");


        private final String parameter;

        HouseQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
        }
}
