package com.softserveinc.ita.homeproject.application.service.impl;

import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.application.service.EntitySpecificationService;
import com.softserveinc.ita.homeproject.application.service.QueryApiService;
import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * QueryApiServiceImpl class is implementation of QueryApiService - service
 * that provides Spring Data Page by request query
 *
 * @author Oleksii Zinkevych
 * @see com.softserveinc.ita.homeproject.application.service.QueryApiService
 */
@Service
@RequiredArgsConstructor
public class QueryApiServiceImpl<T extends BaseEntity> implements QueryApiService<T> {
    private final EntitySpecificationService<T> specificationService;

    @Override
    public Specification<T> getSpecification(UriInfo uriInfo) {
        String filter = QueryApiService.getParameterValue(DefaultQueryParams.FILTER.getParameter(), uriInfo)
            .orElse(null);
        String sort = QueryApiService.getParameterValue(DefaultQueryParams.SORT.getParameter(), uriInfo)
            .orElse(DefaultQueryParams.SORT.getValue());
        return specificationService.getSpecification(QueryApiService.getFilterMap(uriInfo), filter, sort)
            .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
    }
}
