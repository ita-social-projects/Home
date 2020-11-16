package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.model.News;

import java.util.Collection;

public interface GenericServiceInterface<T, V>{
    T create(V payload);
    T update(Long id, V payload);
    Collection<T> getAll();
    T getById(Long id);
    boolean deleteById(Long id);
}
