package com.softserveinc.ita.homeproject.application.config.query;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Apartment;
import org.springframework.stereotype.Component;

@Component
public class ApartmentQueryConfig implements QueryConfig<Apartment> {

    @Override
    public Class<Apartment> getEntityClass() {
        return Apartment.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(ApartmentQueryParamEnum.values());
    }

    public enum ApartmentQueryParamEnum implements QueryParamEnum {

        ID("id"),
        HOUSE_ID("house.id"),
        APARTMENT_NUMBER("apartmentNumber"),
        APARTMENT_AREA("apartmentArea");


        private final String parameter;

        ApartmentQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }

    }
}
