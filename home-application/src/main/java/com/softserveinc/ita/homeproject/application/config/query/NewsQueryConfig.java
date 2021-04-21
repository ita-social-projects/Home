package com.softserveinc.ita.homeproject.application.config.query;

import java.util.Arrays;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import org.springframework.stereotype.Component;

@Component
public class NewsQueryConfig implements QueryConfig<News> {

    @Override
    public Class<News> getEntityClass() {
        return News.class;
    }

    @Override
    public List<QueryParamEnum> getWhiteListEnums() {
        return Arrays.asList(NewsQueryParamEnum.values());
    }

    public enum NewsQueryParamEnum implements QueryParamEnum {

        ID("id"),
        TEXT("text"),
        TITLE("title"),
        SOURCE("source");

        private final String parameter;

        NewsQueryParamEnum(String parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getParameter() {
            return this.parameter;
        }
    }
}
