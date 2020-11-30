package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.homeservice.constants.Timezone;
import com.softserveinc.ita.homeproject.homeservice.dto.UpdateNewsDto;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.News;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateNewsDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadNewsDto;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsApiServiceImpl implements NewsApiService {

    private final NewsService newsService;

    public NewsApiServiceImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public Response addNews(CreateNews createNews) {
        CreateNewsDto createNewsDto = CreateNewsDto.builder()
                .title(createNews.getTitle())
                .description(createNews.getDescription())
                .text(createNews.getText())
                .photoUrl(createNews.getPhotoUrl())
                .source(createNews.getSource())
                .build();
        News response = convertToNewsView(newsService.create(createNewsDto));
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteNews(Long id) {
        newsService.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response getAllNews(@Min(1)Integer pageNumber, @Min(0) @Max(10)Integer pageSize) {
        List<ReadNews> readNewsResponseList = newsService.getAll(pageNumber, pageSize).stream()
                .map(newsDto -> convertToReadNews(newsDto))
                .collect(Collectors.toList());
        return Response.ok().entity(readNewsResponseList).build();
    }

    @Override
    public Response getNews(Long id) {
        ReadNewsDto readNewsDto = newsService.getById(id);
        ReadNews newsApiResponse = convertToReadNews(readNewsDto);
        return Response.ok().entity(newsApiResponse).build();
    }

    @Override
    public Response updateNews(Long id, UpdateNews updateNews) {
        UpdateNewsDto updateNewsDto = UpdateNewsDto.builder()
                .title(updateNews.getTitle())
                .description(updateNews.getDescription())
                .text(updateNews.getText())
                .photoUrl(updateNews.getPhotoUrl())
                .source(updateNews.getSource())
                .build();

        News response = convertToNewsView(newsService.update(id, updateNewsDto));
        return Response.ok().entity(response).build();
    }

    private ReadNews convertToReadNews(ReadNewsDto readNewsDto) {
        ReadNews toResponse = new ReadNews();
        if ((Optional.ofNullable(readNewsDto.getUpdatedAt()).isPresent())) {
            toResponse.setCreateOrUpdateDate(OffsetDateTime.of(readNewsDto.getUpdatedAt(), Timezone.UTC_TIMEZONE));
        } else {
            toResponse.setCreateOrUpdateDate(OffsetDateTime.of(readNewsDto.getCreatedAt(), Timezone.UTC_TIMEZONE));
        }
        toResponse.setId(readNewsDto.getId());
        toResponse.setTitle(readNewsDto.getTitle());
        toResponse.setDescription(readNewsDto.getDescription());
        toResponse.setText(readNewsDto.getText());
        toResponse.setPhotoUrl(readNewsDto.getPhotoUrl());
        toResponse.setSource(readNewsDto.getSource());
        return toResponse;
    }

    private News convertToNewsView(ReadNewsDto readNewsDto) {
        News toView = new News();
        toView.setId(readNewsDto.getId());
        toView.setCreateDate(OffsetDateTime.of(readNewsDto.getCreatedAt(), Timezone.UTC_TIMEZONE));
        toView.setTitle(readNewsDto.getTitle());
        toView.setDescription(readNewsDto.getDescription());
        toView.setText(readNewsDto.getText());
        toView.setPhotoUrl(readNewsDto.getPhotoUrl());
        toView.setSource(readNewsDto.getSource());
        if(Objects.nonNull(readNewsDto.getUpdatedAt()))
            toView.setUpdateDate(OffsetDateTime.of(readNewsDto.getUpdatedAt(), Timezone.UTC_TIMEZONE));
        return toView;
    }
}
