package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.CreateOrUpdateNewsDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadNewsDto;
import org.springframework.data.domain.Page;

public interface NewsService {
    ReadNewsDto create(CreateOrUpdateNewsDto payload);
    ReadNewsDto update(Long id, CreateOrUpdateNewsDto payload);
    Page<ReadNewsDto> getAll(Integer pageNumber, Integer pageSize);
    ReadNewsDto getById(Long id);
    void deleteById(Long id);
}
