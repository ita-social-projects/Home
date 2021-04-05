package com.softserveinc.ita.homeproject.application.service;

import javax.ws.rs.core.MultivaluedMap;

import org.springframework.data.jpa.domain.Specification;

/**
 * EntitySpecificationService - service that provides Spring Data Specification
 * by query parameters
 *
 * @param <T> - entity type
 * @author Oleksii Zinkevych
 * @see org.springframework.data.jpa.domain.Specification
 */
public interface EntitySpecificationService<T> {
    Specification<T> getSpecification(MultivaluedMap<String, String> filter, String search, String sort);
}
