package com.softserveinc.ita.homeproject.application.service;

import javax.ws.rs.core.MultivaluedMap;

import org.springframework.data.jpa.domain.Specification;

/**
 * EntitySpecificationService - service that provides Spring Data Specification
 * by query parameters
 *
 * @author Oleksii Zinkevych
 * @param <T> - entity type
 * @see org.springframework.data.jpa.domain.Specification
 */
public interface EntitySpecificationService<T> {
    /**
     *
     * @param filter - Map of query and path parameters required to build Specification
     * @param search - String filter value in a format according to RSQL documentation
     * @param sort   - String sort order value in a format according to RSQL documentation
     * @return Spring Data Specification
     */
    Specification<T> getSpecification(MultivaluedMap<String, String> filter, String search, String sort);
}
