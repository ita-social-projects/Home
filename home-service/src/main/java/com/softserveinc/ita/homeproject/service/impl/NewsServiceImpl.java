package com.softserveinc.ita.homeproject.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.service.NewsService;
import com.softserveinc.ita.homeproject.service.dto.CreateOrUpdateNewsDto;
import com.softserveinc.ita.homeproject.service.dto.ReadNewsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
        News newsToUpdate = newsRepository.findById(id).orElseThrow();
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
    public Collection<ReadNewsDto> getAll() {

       List<News> entityCollection = new ArrayList<>();
        for (News news : newsRepository.findAll()) {
            entityCollection.add(news);
        }

        return entityCollection.stream()
                .map(entity -> ConvertToGetNewsServiceDto(entity)).collect(Collectors.toList());
    }

    @Override
    public ReadNewsDto getById(Long id) {
        News newsResponse = newsRepository.findById(id).orElseThrow();

        return ConvertToGetNewsServiceDto(newsResponse);
    }

    @Override
    public boolean deleteById(Long id) {
        if (newsRepository.findById(id).isPresent()) {
            newsRepository.deleteById(id);
            return true;
        } else {
            return  false;
        }
    }

    private ReadNewsDto ConvertToGetNewsServiceDto(News newsResponse) {
        ReadNewsDto toApiService = ReadNewsDto.builder()
                .id(newsResponse.getId())
                .title(newsResponse.getTitle())
                .text(newsResponse.getText())
                .description(newsResponse.getDescription())
                .photoUrl(newsResponse.getPhotoUrl())
                .source(newsResponse.getSource())
                .build();

        Optional<LocalDateTime> latestChangeDate = Optional.ofNullable(newsResponse.getUpdateDate());    ;
        if (latestChangeDate.isEmpty()) {
            latestChangeDate = Optional.of(newsResponse.getCreateDate());
        } else{
            toApiService.setUpdatedAt(latestChangeDate.get());
        }
        toApiService.setCreatedAt(newsResponse.getCreateDate());

        return toApiService;
    }
}
