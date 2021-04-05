package com.softserveinc.ita.homeproject.application.service.impl;

import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.application.service.QueryApiService;
import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
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
public class QueryApiServiceImpl<T extends BaseEntity, D extends BaseDto> implements QueryApiService<T, D> {
    private final EntitySpecificationServiceImpl<T> specificationService;

    @Override
    public Specification<T> getSpecification(UriInfo uriInfo) {
        return specificationService.getSpecification(
                QueryApiService.getFilterMap(uriInfo),
                QueryApiService.getParameterValue(DefaultQueryParams.FILTER.getParameter(), uriInfo),
                QueryApiService.getParameterValue(DefaultQueryParams.SORT.getParameter(), uriInfo));
    }
}
