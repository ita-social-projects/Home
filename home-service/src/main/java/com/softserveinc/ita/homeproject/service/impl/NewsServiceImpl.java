package com.softserveinc.ita.homeproject.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.service.NewsService;
import com.softserveinc.ita.homeproject.service.dto.ServiceNewsDto;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public News create(ServiceNewsDto payload) {
        return null;
    }

    @Override
    public News update(Long id, ServiceNewsDto payload) {
        return null;
    }

    @Override
    public Collection<News> getAll() {
        return (Collection<News>) newsRepository.findAll();
    }

    @Override
    public News getById(Long id) {
        return newsRepository.findById(id).orElseThrow();
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
}
