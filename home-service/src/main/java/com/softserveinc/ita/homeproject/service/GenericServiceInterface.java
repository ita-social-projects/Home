package com.softserveinc.ita.homeproject.service;

import java.util.Collection;

public interface GenericServiceInterface<T, V, E>{
    T create(V payload);
    T update(Long id, E payload);
    Collection<T> getAll();
    T getById(Long id);
    boolean deleteById(Long id);
}
