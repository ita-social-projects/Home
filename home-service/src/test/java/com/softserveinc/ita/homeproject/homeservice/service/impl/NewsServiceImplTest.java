package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homedata.repository.NewsRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class NewsServiceImplTest {

    @Mock
    private static ServiceMapper serviceMapper;

    @Mock
    private static NewsRepository newsRepository;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setMocksBehavior() {
        when(serviceMapper.convert(any(), any())).thenReturn(new NewsDto());
    }

    @Test
    void getOneTestWhenPageHaveMoreThanOneElement() {
        Page<News> page = new PageImpl<>(Arrays.asList(new News(), new News()));
        when(newsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        assertThrows(IllegalStateException.class, () -> newsService.getOne(1L));
    }
}
