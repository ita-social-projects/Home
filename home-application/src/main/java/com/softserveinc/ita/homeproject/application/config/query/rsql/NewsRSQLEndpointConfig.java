package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.NewsQueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.NewsQueryConfig.NewsQueryParamEnum;
import com.softserveinc.ita.homeproject.application.config.query.QueryConfig;
import com.softserveinc.ita.homeproject.homedata.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsRSQLEndpointConfig implements RSQLEndpointConfig<News, NewsQueryConfig.NewsQueryParamEnum> {

    @Autowired
    private NewsQueryConfig queryConfig;

    @Override
    public Map<NewsQueryParamEnum, String> getMappings() {
        EnumMap<NewsQueryParamEnum, String> map = new EnumMap<>(NewsQueryParamEnum.class);

        map.put(NewsQueryConfig.NewsQueryParamEnum.SOURCE, "source");
        map.put(NewsQueryConfig.NewsQueryParamEnum.TEXT, "text");
        map.put(NewsQueryConfig.NewsQueryParamEnum.TITLE, "title");
        return map;
    }

    @Override
    public QueryConfig<News> getQueryConfig() {
        return queryConfig;
    }
}
