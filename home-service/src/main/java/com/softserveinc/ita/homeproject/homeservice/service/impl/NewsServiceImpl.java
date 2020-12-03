package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundException;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional
    public NewsDto create(NewsDto newsDto) {
        News news = News.builder()
                .description(newsDto.getDescription())
                .title(newsDto.getTitle())
                .text(newsDto.getText())
                .createDate(LocalDateTime.now())
                .photoUrl(newsDto.getPhotoUrl())
                .source(newsDto.getSource())
                .build();
        newsRepository.save(news);

        return convertToGetNewsServiceDto(news);
    }

    @Override
    @Transactional
    public NewsDto update(Long id, NewsDto newsDto)  {

        if (newsRepository.findById(id).isPresent()) {

            News fromDB = newsRepository.findById(id).get();

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
            return convertToGetNewsServiceDto(fromDB);

        } else {
            throw new NotFoundException("User with id:" + id + " is not found");
        }
    }

    @Override
    public Page<NewsDto> getAll(Integer pageNumber, Integer pageSize) {
        return newsRepository.findAll(PageRequest.of(pageNumber-1, pageSize))
                .map(page -> convertToGetNewsServiceDto(page));
    }

    @Override
    public NewsDto getById(Long id) {
        News newsResponse = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));

        return convertToGetNewsServiceDto(newsResponse);
    }

    @Override
    public void deleteById(Long id) {
            newsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));
            newsRepository.deleteById(id);
    }

    private NewsDto convertToGetNewsServiceDto(News news) {
       return NewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .text(news.getText())
                .description(news.getDescription())
                .photoUrl(news.getPhotoUrl())
                .source(news.getSource())
                .build();
    }
}
