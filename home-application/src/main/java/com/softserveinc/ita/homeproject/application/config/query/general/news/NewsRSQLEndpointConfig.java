package com.softserveinc.ita.homeproject.application.config.query.general.news;

import java.util.EnumMap;
import java.util.Map;

import com.softserveinc.ita.homeproject.application.config.query.base.QueryConfig;
import com.softserveinc.ita.homeproject.application.config.query.base.RSQLEndpointConfig;
import com.softserveinc.ita.homeproject.application.config.query.general.news.NewsQueryConfig.NewsQueryParamEnum;
import com.softserveinc.ita.homeproject.homedata.general.entity.news.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsRSQLEndpointConfig implements RSQLEndpointConfig<News, NewsQueryParamEnum> {

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
