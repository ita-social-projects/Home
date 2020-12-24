package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundException;
import com.softserveinc.ita.homeproject.homeservice.mapperentity.NewsMapper;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    @Transactional
    public NewsDto create(NewsDto newsDto) {
        News news = newsMapper.convertDtoToEntity(newsDto);
        news.setCreateDate(LocalDateTime.now());

        newsRepository.save(news);

        return newsMapper.convertEntityToDto(news);
        return convertToGetNewsServiceDto(news);
    }

    @Override
    @Transactional
    public NewsDto update(Long id, NewsDto newsDto) {
        News newsToUpdate = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));
        newsToUpdate.setTitle(newsDto.getTitle());
        newsToUpdate.setText(newsDto.getText());
        newsToUpdate.setUpdateDate(LocalDateTime.now());
        newsToUpdate.setDescription(newsDto.getDescription());
        newsToUpdate.setPhotoUrl(newsDto.getPhotoUrl());
        newsToUpdate.setSource(newsDto.getSource());
        newsRepository.save(newsToUpdate);
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
        return newsRepository.findAll(PageRequest.of(pageNumber - 1, pageSize))
                .map(newsMapper::convertEntityToDto);
        return newsRepository.findAll(PageRequest.of(pageNumber-1, pageSize))
                .map(page -> convertToGetNewsServiceDto(page));
    }

    @Override
    public NewsDto getById(Long id) {
        News newsResponse = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));

        return newsMapper.convertEntityToDto(newsResponse);
        return convertToGetNewsServiceDto(newsResponse);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find news with given ID:" + id));
        newsRepository.deleteById(id);
    }

    private NewsDto ConvertToGetNewsServiceDto(News news) {
        return NewsDto.builder()
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
