package com.softserveinc.ita.homeproject.application.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.springframework.data.domain.Page;

/**
 * QueryApiService - service that provides Spring Data Page by request query
 *
 * @author Oleksii Zinkevych
 * @see javax.ws.rs.core.UriInfo
 */
public interface QueryApiService<T extends BaseEntity, D extends BaseDto> {
    Set<String> EXCLUDED_PARAMETERS = new HashSet<>(Arrays.stream(DefaultQueryParams.values())
            .map(DefaultQueryParams::getParameter)
            .collect(Collectors.toList()));

    /***
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @param service - implementation of QueryableService interface
     * @return Spring Data Page of DTOs according to request query
     */
    Page<D> getPageFromQuery(UriInfo uriInfo, QueryableService<T, D> service);

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Integer value of Spring Data Page number specified in a request
     */
    default Integer getPageNumber(UriInfo uriInfo) {
        return Integer.valueOf(uriInfo.getQueryParameters()
                .get(DefaultQueryParams.PAGE_NUMBER.getParameter())
                .get(0));
    }

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Integer value of Spring Data Page size specified in a request
     */
    default Integer getPageSize(UriInfo uriInfo) {
        return Integer.valueOf(uriInfo.getQueryParameters()
                .get(DefaultQueryParams.PAGE_SIZE.getParameter())
                .get(0));
    }

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return String value of Filter parameter specified in a request
     */
    default String getFilter(UriInfo uriInfo) {
        if (uriInfo.getQueryParameters().get(DefaultQueryParams.FILTER.getParameter()) != null) {
            return uriInfo.getQueryParameters()
                    .get(DefaultQueryParams.FILTER.getParameter())
                    .get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return String value of Sort parameter specified in a request
     */
    default String getSort(UriInfo uriInfo) {
        if (uriInfo.getQueryParameters().get(DefaultQueryParams.SORT.getParameter()) != null) {
            return uriInfo.getQueryParameters()
                    .get(DefaultQueryParams.SORT.getParameter())
                    .get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Map of query and path parameters required by EntitySpecificationService
     * to build Filter specification
     */
    default Map<String, String> getFilterMap(UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        MultivaluedMap<String, String> pathParameters = uriInfo.getPathParameters();
        Map<String, String> filterMap = new HashMap<>();
        queryParameters.forEach((key, value) -> {
            if(!(EXCLUDED_PARAMETERS.contains(key))) {
                filterMap.put(key, value.get(0));
            }
        });
        pathParameters.forEach((key, value) -> {
            filterMap.put(key, value.get(0));
        });
        return filterMap;
    }

    /**
     * Enum of default query parameters that have to be excluded from filter map
     */
    enum DefaultQueryParams {
        PAGE_NUMBER("page_number"),
        PAGE_SIZE("page_size"),
        FILTER("filter"),
        SORT("sort");

        private final String parameter;

        DefaultQueryParams(String parameter) {
            this.parameter = parameter;
        }

        public String getParameter() {
            return this.parameter;
        }
    }
}
