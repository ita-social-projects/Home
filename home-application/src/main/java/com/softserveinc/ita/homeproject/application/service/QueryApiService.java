package com.softserveinc.ita.homeproject.application.service;

import static java.util.function.Predicate.not;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import org.springframework.data.jpa.domain.Specification;

/**
 * QueryApiService - service that provides Spring Data Page by request query
 * @author Oleksii Zinkevych
 * @param <T> - entity type
 * @see javax.ws.rs.core.UriInfo
 */
public interface QueryApiService<T extends BaseEntity> {
    Map<String, String> EXCLUDED_PARAMETERS = Arrays.stream(DefaultQueryParams.values())
            .collect(Collectors.toMap(DefaultQueryParams::getParameter, DefaultQueryParams::getValue));

    /**
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Map of query and path parameters required by EntitySpecificationService
     * to build Filter specification
     * @throws BadRequestHomeException if uriInfo contains path and query params with the same name
     * @throws BadRequestHomeException if uriInfo contains query params with more than one value
     */
    static MultivaluedMap<String, String> getFilterMap(UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        queryParams.forEach(QueryApiService::validateQueryParamValue);
        return Stream.of(uriInfo.getPathParameters(), queryParams)
                .flatMap(map -> map.entrySet().stream())
                .filter(not(entry -> EXCLUDED_PARAMETERS.containsKey(entry.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new BadRequestHomeException("Request contains same path and query params");
                        },
                        MultivaluedHashMap::new
                ));
    }

    private static void validateQueryParamValue(String queryParamName, List<String> queryParamValue) {
        if (queryParamValue.size() > 1) {
            throw new BadRequestHomeException("Query param '" + queryParamName + "' has more than one value");
        }
    }

    /**
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Spring Data Specification of entity
     */
    Specification<T> getSpecification(UriInfo uriInfo);

    /**
     * @param param   - String name of query or path parameter
     * @param uriInfo - object that implements UriInfo interface
     *                  that provides access to application and request URI information
     * @return Optional of String value of specified query parameter
     */
    static Optional<String> getParameterValue(String param, UriInfo uriInfo) {
        if (uriInfo.getQueryParameters().get(param) != null) {
            return Optional.of(uriInfo.getQueryParameters()
                    .get(param)
                    .get(0));
        } else if (uriInfo.getPathParameters().get(param) != null) {
            return Optional.of(uriInfo.getPathParameters()
                    .get(param)
                    .get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Enum of default query parameters that have to be excluded from filter map
     */
    enum DefaultQueryParams {
        PAGE_NUMBER("page_number", "1"),
        PAGE_SIZE("page_size", "5"),
        FILTER("filter", ""),
        SORT("sort", "id,desc");

        private final String parameter;
        private final String value;

        DefaultQueryParams(String parameter, String value) {
            this.parameter = parameter;
            this.value = value;
        }

        public String getParameter() {
            return this.parameter;
        }

        public String getValue() {
            return this.value;
        }
    }
}
