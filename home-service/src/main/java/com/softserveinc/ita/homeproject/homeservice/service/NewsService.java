package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface NewsService {
    NewsDto create(NewsDto newsDto);

    NewsDto update(Long id, NewsDto newsDto);

    Page<NewsDto> findNews(Integer pageNumber, Integer pageSize, Specification<News> specification);

    NewsDto getById(Long id);

    void deleteById(Long id);
}
