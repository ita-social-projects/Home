package com.softserveinc.ita.homeproject.homeservice.service;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * QueryableService - service that provides Spring Data Page by entity specification
 * and page parameters
 * @author Oleksii Zinkevych
 * @param <T> - entity type
 * @param <D> - DTO type
 */
public interface QueryableService<T extends BaseEntity, D extends BaseDto> {
    Page<D> findAll(Integer pageNumber, Integer pageSize, Specification<T> specification);
}
