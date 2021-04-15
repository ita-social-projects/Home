package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface NewsService extends QueryableService<News, NewsDto> {
    NewsDto create(NewsDto newsDto);

    NewsDto update(Long id, NewsDto newsDto);

    Page<NewsDto> findAll(Integer pageNumber, Integer pageSize, Specification<News> specification);

    NewsDto getById(Long id);

    void deactivateNews(Long id);
}
