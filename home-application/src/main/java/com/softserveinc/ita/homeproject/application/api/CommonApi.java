package com.softserveinc.ita.homeproject.application.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.service.QueryApiService;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class CommonApi {

    private static final String PAGING_COUNT = "Paging-count";

    private static final String PAGING_TOTAL_PAGES = "Paging-total-pages";

    private static final String PAGING_TOTAL_COUNT = "Paging-total-count";

    @Context
    private UriInfo uriInfo;

    @Autowired
    protected HomeMapper mapper;

    @Autowired
    private QueryApiService queryApiService;

    protected <D extends BaseDto> Response buildQueryResponse(Page<D> page, Class clazz) {
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

    protected <T> Specification<T> getSpecification() {
        return queryApiService.getSpecification(uriInfo);
    }

    public void verifyExistence(Long parentId, QueryableService service){
        service.getOne(parentId);
    }

    /**
     * Verify existing of parent dto, when parentId is send by path param.
     * Example: /cooperation/{cooperation_id}/houses/{id}
     * here cooperation_id - is parentId and if it doesn't exist exception 404 (not found) is thrown.
     * @param parentId
     * @param childId
     * @param parentService
     * @param childService
     * @return
     */
    public BaseDto getChildDto(Long parentId,
                               Long childId,
                               QueryableService parentService,
                               QueryableService childService) {
        verifyExistence(parentId, parentService);
        return childService.getOne(childId, getSpecification());
    }

    public Page<? extends BaseDto> getAllChildDtos(Long parentId,
                                                   Integer pageNumber,
                                                   Integer pageSize,
                                                   QueryableService parentService,
                                                   QueryableService childService){
        verifyExistence(parentId, parentService);
        return childService.findAll(pageNumber, pageSize, getSpecification());
    }
}
