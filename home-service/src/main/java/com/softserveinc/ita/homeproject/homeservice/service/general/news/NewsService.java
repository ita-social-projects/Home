package com.softserveinc.ita.homeproject.homeservice.service.general.news;

import com.softserveinc.ita.homeproject.homedata.general.news.News;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface NewsService extends QueryableService<News, NewsDto> {
    NewsDto create(NewsDto newsDto);

    NewsDto update(Long id, NewsDto newsDto);

    void deactivateNews(Long id);
}
