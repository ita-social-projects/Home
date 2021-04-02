package com.softserveinc.ita.homeproject.application.service.impl;

import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.application.service.QueryApiService;
import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class QueryApiServiceImpl<T extends BaseEntity, D extends BaseDto> implements QueryApiService<T, D> {
    private final EntitySpecificationService<T> specificationService;

    @Autowired
    public QueryApiServiceImpl(EntitySpecificationService<T> specificationService) {
        this.specificationService = specificationService;
    }

    @Override
    public Page<D> getPageFromQuery(UriInfo uriInfo, QueryableService<T, D> service) {
        return service.findAll(getPageNumber(uriInfo), getPageSize(uriInfo), getSpecification(uriInfo));
    }

    private Specification<T> getSpecification(UriInfo uriInfo) {
        return specificationService.getSpecification(getFilterMap(uriInfo), getFilter(uriInfo), getSort(uriInfo));
    }
}
