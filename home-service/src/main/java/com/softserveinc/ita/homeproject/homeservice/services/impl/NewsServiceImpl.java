package com.softserveinc.ita.homeproject.homeservice.services.impl;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.homeservice.services.NewsService;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateOrUpdateNewsDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadNewsDto;
import com.softserveinc.ita.homeproject.homeservice.exceptions.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional
    public ReadNewsDto create(CreateOrUpdateNewsDto createNewsServiceDto) {
        News news = News.builder()
                .description(createNewsServiceDto.getDescription())
                .title(createNewsServiceDto.getTitle())
                .text(createNewsServiceDto.getText())
                .createDate(createNewsServiceDto.getDateTime())
                .photoUrl(createNewsServiceDto.getPhotoUrl())
                .source(createNewsServiceDto.getSource())
                .build();
        newsRepository.save(news);

        return ConvertToGetNewsServiceDto(news);
    }

    @Override
    @Transactional
    public ReadNewsDto update(Long id, CreateOrUpdateNewsDto updateNewsServiceDto)  {
        News newsToUpdate = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID"));
        newsToUpdate.setTitle(updateNewsServiceDto.getTitle());
        newsToUpdate.setText(updateNewsServiceDto.getText());
        newsToUpdate.setUpdateDate(updateNewsServiceDto.getDateTime());
        newsToUpdate.setDescription(updateNewsServiceDto.getDescription());
        newsToUpdate.setPhotoUrl(updateNewsServiceDto.getPhotoUrl());
        newsToUpdate.setSource(updateNewsServiceDto.getSource());
        newsRepository.save(newsToUpdate);

        return ConvertToGetNewsServiceDto(newsToUpdate);
    }

    @Override
    public Collection<ReadNewsDto> getAll(Integer pageNumber, Integer pageSize) {

       List<News> newsList = new ArrayList<>();
        for (News news : newsRepository.findAll(PageRequest.of(pageNumber, pageSize))) {
            newsList.add(news);
        }

        return newsList.stream()
                .map(entity -> ConvertToGetNewsServiceDto(entity)).collect(Collectors.toList());
    }

    @Override
    public ReadNewsDto getById(Long id) {
        News newsResponse = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID"));

        return ConvertToGetNewsServiceDto(newsResponse);
    }

    @Override
    public void deleteById(Long id) {
            newsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Can't find news with given ID"));
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
