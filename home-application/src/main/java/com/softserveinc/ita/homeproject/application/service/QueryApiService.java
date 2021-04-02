package com.softserveinc.ita.homeproject.application.service;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.springframework.data.domain.Page;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface QueryApiService<T extends BaseEntity, D extends BaseDto> {
    Set<String> EXCLUDED_PARAMS_SET = new HashSet<>(Arrays.asList(
            DefaultQueryParams.PAGE_NUMBER.getParameter(),
            DefaultQueryParams.PAGE_SIZE.getParameter(),
            DefaultQueryParams.FILTER.getParameter(),
            DefaultQueryParams.SORT.getParameter()));

    Page<D> getPageFromQuery(UriInfo uriInfo, QueryableService<T, D> service);

    default Integer getPageNumber(UriInfo uriInfo) {
        return Integer.valueOf(uriInfo.getQueryParameters().get(DefaultQueryParams.PAGE_NUMBER.getParameter()).get(0));
    }

    default Integer getPageSize(UriInfo uriInfo) {
        return Integer.valueOf(uriInfo.getQueryParameters().get(DefaultQueryParams.PAGE_SIZE.getParameter()).get(0));
    }

    default String getFilter(UriInfo uriInfo) {
        if (uriInfo.getQueryParameters().get(DefaultQueryParams.FILTER.getParameter()) != null) {
            return uriInfo.getQueryParameters().get(DefaultQueryParams.FILTER.getParameter()).get(0);
        } else {
            return null;
        }
    }

    default String getSort(UriInfo uriInfo) {
        if (uriInfo.getQueryParameters().get(DefaultQueryParams.SORT.getParameter()) != null) {
            return uriInfo.getQueryParameters().get(DefaultQueryParams.SORT.getParameter()).get(0);
        } else {
            return null;
        }
    }

    default Map<String, String> getFilterMap(UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        MultivaluedMap<String, String> pathParameters = uriInfo.getPathParameters();
        Map<String, String> filterMap = new HashMap<>();
        queryParameters.forEach((key, value) -> {
            if(!(EXCLUDED_PARAMS_SET.contains(key))) {
                filterMap.put(key, value.get(0));
            }
        });
        pathParameters.forEach((key, value) -> {
            filterMap.put(key, value.get(0));
        });
        return filterMap;
    }

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
