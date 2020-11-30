package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.UpdateNewsDto;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateNewsDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadNewsDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional
    public ReadNewsDto create(CreateNewsDto createNewsDto) {
        createNewsDto.setCreateTime(LocalDateTime.now());
        News news = News.builder()
                .description(createNewsDto.getDescription())
                .title(createNewsDto.getTitle())
                .text(createNewsDto.getText())
                .createDate(createNewsDto.getCreateTime())
                .photoUrl(createNewsDto.getPhotoUrl())
                .source(createNewsDto.getSource())
                .build();
        newsRepository.save(news);

        return ConvertToGetNewsServiceDto(news);
    }

    @Override
    @Transactional
    public ReadNewsDto update(Long id, UpdateNewsDto updateNewsDto)  {
        updateNewsDto.setUpdateTime(LocalDateTime.now());
        News newsToUpdate = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));
        newsToUpdate.setTitle(updateNewsDto.getTitle());
        newsToUpdate.setText(updateNewsDto.getText());
        newsToUpdate.setUpdateDate(updateNewsDto.getUpdateTime());
        newsToUpdate.setDescription(updateNewsDto.getDescription());
        newsToUpdate.setPhotoUrl(updateNewsDto.getPhotoUrl());
        newsToUpdate.setSource(updateNewsDto.getSource());
        newsRepository.save(newsToUpdate);

        return ConvertToGetNewsServiceDto(newsToUpdate);
    }

    @Override
    public Page<ReadNewsDto> getAll(Integer pageNumber, Integer pageSize) {
        return newsRepository.findAll(PageRequest.of(pageNumber-1, pageSize))
                .map(page -> ConvertToGetNewsServiceDto(page));
    }

    @Override
    public ReadNewsDto getById(Long id) {
        News newsResponse = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));

        return ConvertToGetNewsServiceDto(newsResponse);
    }

    @Override
    public void deleteById(Long id) {
            newsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));
            newsRepository.deleteById(id);
    }

    private ReadNewsDto ConvertToGetNewsServiceDto(News news) {
        ReadNewsDto toApiService = ReadNewsDto.builder()
                .id(news.getId())
                .createdAt(news.getCreateDate())
                .title(news.getTitle())
                .text(news.getText())
                .description(news.getDescription())
                .photoUrl(news.getPhotoUrl())
                .source(news.getSource())
                .build();

        Optional.ofNullable(news.getUpdateDate()).ifPresent(toApiService::setUpdatedAt);

        return toApiService;
    }
}
