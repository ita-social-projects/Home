package com.softserveinc.ita.homeproject.application.apiservice;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.DtoToViewMapper;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import org.springframework.data.domain.Page;

public abstract class CommonApiService<R extends BaseDto> {

    public static final String PAGING_COUNT = "Paging-count";
    public static final String PAGING_TOTAL_PAGES = "Paging-total-pages";
    public static final String PAGING_TOTAL_COUNT = "Paging-total-count";

    public abstract DtoToViewMapper<R, ?> getDtoToViewMapper();

    Response buildQueryResponse(Page<R> page) {
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        int numberOfElements = page.getNumberOfElements();

        List<?> pageElements = page.stream()
                .map(getDtoToViewMapper()::convertDtoToView)
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK)
                .entity(pageElements)
                .header(PAGING_COUNT, numberOfElements)
                .header(PAGING_TOTAL_PAGES, totalPages)
                .header(PAGING_TOTAL_COUNT, totalElements)
                .build();
    }
}
