package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.model.BaseReadView;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public abstract class CommonApi<R extends BaseDto> {

    public static final String PAGING_COUNT = "Paging-count";
    public static final String PAGING_TOTAL_PAGES = "Paging-total-pages";
    public static final String PAGING_TOTAL_COUNT = "Paging-total-count";

    @Autowired
    protected HomeMapper mapper;

    @Autowired
    protected EntitySpecificationService entitySpecificationService;

    Response buildQueryResponse(Page<R> page, Class<? extends BaseReadView> clazz) {
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        int numberOfElements = page.getNumberOfElements();

        List<?> pageElements = page.stream()
                .map(p -> mapper.convert(p, clazz))
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK)
                .entity(pageElements)
                .header(PAGING_COUNT, numberOfElements)
                .header(PAGING_TOTAL_PAGES, totalPages)
                .header(PAGING_TOTAL_COUNT, totalElements)
                .build();
    }
}
