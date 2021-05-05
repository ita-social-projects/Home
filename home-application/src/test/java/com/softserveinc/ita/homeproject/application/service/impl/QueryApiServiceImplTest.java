package com.softserveinc.ita.homeproject.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.softserveinc.ita.homeproject.application.service.EntitySpecificationService;
import com.softserveinc.ita.homeproject.application.service.QueryApiService;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SuppressWarnings({"rawtypes", "unchecked"})
class QueryApiServiceImplTest {

    @Mock
    private static EntitySpecificationService entitySpecificationService;

    @Mock
    private static UriInfo uriInfo;

    @Mock
    private static QueryableService queryableService;

    @Mock
    private static Specification specification;

    @InjectMocks
    private QueryApiServiceImpl queryApiService;

    @Test
    void getFilterMapTestWhenQueryOrPathParamsHaveTwoValues() {
        when(uriInfo.getPathParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("cooperation_id", Arrays.asList("11", "11"));
            put("user_id", Arrays.asList("11", "11"));
        }});
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("id", Arrays.asList("11", "11"));
            put("size", Arrays.asList("11", "11"));
        }});
        assertThrows(BadRequestHomeException.class, () -> QueryApiService.getFilterMap(uriInfo));
    }

    @Test
    void getFilterMapTestWhenPathAndQueryParamsHaveTheSameName() {
        when(uriInfo.getPathParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("id", Collections.singletonList("1"));
        }});
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("id", Collections.singletonList("1"));
        }});
        assertThrows(BadRequestHomeException.class, () -> QueryApiService.getFilterMap(uriInfo));
    }

    @Test
    void getParameterValueTest() {
        when(uriInfo.getPathParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("id", Collections.singletonList("1"));
            put("key2", Collections.singletonList("22"));
        }});
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("key3", Collections.singletonList("33"));
            put("user_id", Collections.singletonList("4"));
        }});
        assertEquals("1", QueryApiService.getParameterValue("id", uriInfo).orElseThrow());
        assertEquals("4", QueryApiService.getParameterValue("user_id", uriInfo).orElseThrow());
    }

    @Test
    void getOneTestWhenPageHaveMoreThanOneElement() {
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("page_number", Collections.singletonList("1"));
            put("page_size", Collections.singletonList("10"));
            put("filter", Collections.singletonList("filter"));
        }});
        when(uriInfo.getPathParameters()).thenReturn(new MultivaluedHashMap<>() {{
            put("id", Collections.singletonList("1"));
        }});
        Page page = new PageImpl(Arrays.asList("1", "2", "3"));
        when(entitySpecificationService.getSpecification(any(MultivaluedMap.class), anyString(), anyString()))
            .thenReturn(specification);
        when(queryableService.findAll(anyInt(), anyInt(), any(Specification.class))).thenReturn(page);
        assertThrows(IllegalStateException.class, () -> queryApiService.getOne(uriInfo, queryableService));
    }
}
