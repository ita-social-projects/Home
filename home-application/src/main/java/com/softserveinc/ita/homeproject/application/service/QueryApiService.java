package com.softserveinc.ita.homeproject.application.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

/**
 * QueryApiService - service that provides Spring Data Page by request query
 *
 * @author Oleksii Zinkevych
 * @param <T> - entity type
 * @param <D> - DTO type
 * @see javax.ws.rs.core.UriInfo
 */
public interface QueryApiService<T extends BaseEntity, D extends BaseDto> {
    Set<String> EXCLUDED_PARAMETERS = new HashSet<>(Arrays.stream(DefaultQueryParams.values())
            .map(DefaultQueryParams::getParameter)
            .collect(Collectors.toList()));

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Map of query and path parameters required by EntitySpecificationService
     * to build Filter specification
     */
    static MultivaluedMap<String, String> getFilterMap(UriInfo uriInfo) {
        MultivaluedMap<String, String> filterMap = new MultivaluedHashMap<>();
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        MultivaluedMap<String, String> pathParameters = uriInfo.getPathParameters();
        queryParameters.forEach((key, value) -> {
            if(!(EXCLUDED_PARAMETERS.contains(key))) {
                filterMap.put(key, value);
            }
        });
        pathParameters.forEach(filterMap::put);
        return filterMap;
    }

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Spring Data Specification of entity
     */
    Specification<T> getSpecification(UriInfo uriInfo);

    /**
     *
     * @param param   - String name of query parameter
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return String value of specified query parameter
     */
    static String getParameterValue(String param, UriInfo uriInfo) {
        if (uriInfo.getQueryParameters().get(param) != null) {
            return uriInfo.getQueryParameters()
                    .get(param)
                    .get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @param service - implementation of QueryableService interface
     * @return Spring Data Page of DTOs according to request query
     */
    default Page<D> getPageFromQuery(UriInfo uriInfo, QueryableService<T, D> service) {
        return service.findAll(
                Integer.valueOf(Objects.requireNonNull(
                        getParameterValue(DefaultQueryParams.PAGE_NUMBER.getParameter(), uriInfo))),
                Integer.valueOf(Objects.requireNonNull(
                        getParameterValue(DefaultQueryParams.PAGE_SIZE.getParameter(), uriInfo))),
                getSpecification(uriInfo));
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
