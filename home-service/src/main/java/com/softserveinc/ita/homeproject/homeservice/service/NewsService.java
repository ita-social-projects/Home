package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import org.springframework.data.domain.Page;

public interface NewsService {
    NewsDto create(NewsDto newsDto);
    NewsDto update(Long id, NewsDto newsDto);
    Page<NewsDto> getAll(Integer pageNumber, Integer pageSize);
    NewsDto getById(Long id);
    void deleteById(Long id);
}
