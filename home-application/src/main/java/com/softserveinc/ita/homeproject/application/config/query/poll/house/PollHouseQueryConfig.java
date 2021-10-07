package com.softserveinc.ita.homeproject.application.config.query.poll.house;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.application.config.query.poll.PollQueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import org.springframework.stereotype.Component;

@Component
public class PollHouseQueryConfig implements QueryConfig<House> {
    @Override
    public Class<House> getEntityClass() {
        return House.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(PollQueryConfig.PollQueryParamEnum.values());
    }

    public enum PollHouseQueryParamEnum implements QueryParamEnum {
        POLL_ID("polls.id"),
        QUANTITY_FLAT("quantityFlat"),
        ADJOINING_AREA("adjoiningArea"),
        HOUSE_AREA("houseArea");

        private final String parameter;

        PollHouseQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
    }
}
