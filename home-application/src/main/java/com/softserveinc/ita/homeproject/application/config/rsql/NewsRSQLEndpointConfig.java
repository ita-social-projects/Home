package com.softserveinc.ita.homeproject.application.config.rsql;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homeservice.query.QueryConfig;
import com.softserveinc.ita.homeproject.homeservice.query.impl.NewsQueryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NewsRSQLEndpointConfig implements RSQLEndpointConfig<News, NewsQueryConfig.NewsQueryParamEnum> {

    @Autowired
    private NewsQueryConfig queryConfig;

    @Override
    public Map<NewsQueryConfig.NewsQueryParamEnum, String> getMappings() {
        HashMap<NewsQueryConfig.NewsQueryParamEnum, String> map = new HashMap<>();

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
