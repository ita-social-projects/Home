package com.softserveinc.ita.homeproject.application.config.query;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import org.springframework.stereotype.Component;

@Component
public class HouseQueryConfig implements QueryConfig<House> {


    @Override
    public Class<House> getEntityClass() {
        return House.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(HouseQueryParamEnum.values());
    }

    public enum HouseQueryParamEnum implements QueryParamEnum {

        ID("id"),
        COOPERATION_ID("cooperation.id"),
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
