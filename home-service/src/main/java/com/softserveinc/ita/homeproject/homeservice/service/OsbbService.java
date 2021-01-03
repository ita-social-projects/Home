package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.OsbbDto;
import org.springframework.data.domain.Page;

public interface OsbbService {

    OsbbDto createOsbb(OsbbDto createOsbbDto);

    OsbbDto updateOsbb(Long id, OsbbDto updateOsbbDto);

    Page<OsbbDto> getAllOsbb(Integer pageNumber, Integer pageSize);

    OsbbDto getOsbbById(Long id);

    void deactivateOsbb(Long id);
}
