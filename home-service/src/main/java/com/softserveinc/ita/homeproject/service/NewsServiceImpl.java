package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.model.News;

import java.util.Collection;

public class NewsServiceImpl implements NewsService{

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }


    @Override
    public News create(Object payload) {
        return null;
    }

    @Override
    public News update(Long id, Object payload) {
        return null;
    }

    @Override
    public Collection<News> getAll() {
        return null;
    }

    @Override
    public News getById(Long id) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
