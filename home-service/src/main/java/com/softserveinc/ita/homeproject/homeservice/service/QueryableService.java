package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface QueryableService<T extends BaseEntity, D extends BaseDto> {
    Page<D> findAll(Integer pageNumber, Integer pageSize, Specification<T> specification);
}
