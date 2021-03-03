package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private static final String NOT_FOUND_NEWS = "Can't find news with given ID:";

    private final NewsRepository newsRepository;

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public NewsDto create(NewsDto newsDto) {
        News news = mapper.convert(newsDto, News.class);
        news.setCreateDate(LocalDateTime.now());

        newsRepository.save(news);

        return mapper.convert(news, NewsDto.class);
    }

    @Override
    @Transactional
    public NewsDto update(Long id, NewsDto newsDto) {

        Optional<News> optionalNews = newsRepository.findById(id);

        if (optionalNews.isPresent()) {

            News fromDB = optionalNews.get();

            if (newsDto.getTitle() != null) {
                fromDB.setTitle(newsDto.getTitle());
            }

            if (newsDto.getText() != null) {
                fromDB.setText(newsDto.getText());
            }

            if (newsDto.getDescription() != null) {
                fromDB.setDescription(newsDto.getDescription());
            }

            if (newsDto.getPhotoUrl() != null) {
                fromDB.setPhotoUrl(newsDto.getPhotoUrl());
            }

            if (newsDto.getSource() != null) {
                fromDB.setSource(newsDto.getSource());
            }

            fromDB.setUpdateDate(LocalDateTime.now());
            newsRepository.save(fromDB);
            return mapper.convert(fromDB, NewsDto.class);

        } else {
            throw new NotFoundHomeException(NOT_FOUND_NEWS + id);
        }
    }

    @Override
    public Page<NewsDto> findNews(Integer pageNumber, Integer pageSize, Specification<News> specification) {
        return newsRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(news -> mapper.convert(news, NewsDto.class));
    }

    @Override
    public NewsDto getById(Long id) {
        News newsResponse = newsRepository.findById(id)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_NEWS  + id));

        return mapper.convert(newsResponse, NewsDto.class);
    }

    @Override
    public void deactivateNews(Long id) {
        News toDelete = newsRepository.findById(id)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_NEWS  + id));
        toDelete.setEnabled(false);
        newsRepository.save(toDelete);
    }

}
