package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.CreateNewsDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadNewsDto;
import com.softserveinc.ita.homeproject.homeservice.dto.UpdateNewsDto;
import org.springframework.data.domain.Page;

public interface NewsService {
    ReadNewsDto create(CreateNewsDto createNewsDto);
    ReadNewsDto update(Long id, UpdateNewsDto updateNewsDto);
    Page<ReadNewsDto> getAll(Integer pageNumber, Integer pageSize);
    ReadNewsDto getById(Long id);
    void deleteById(Long id);
}
