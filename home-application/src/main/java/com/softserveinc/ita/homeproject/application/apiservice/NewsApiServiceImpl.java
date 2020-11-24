package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.News;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import com.softserveinc.ita.homeproject.service.NewsService;
import com.softserveinc.ita.homeproject.service.dto.CreateOrUpdateNewsDto;
import com.softserveinc.ita.homeproject.service.dto.ReadNewsDto;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NewsApiServiceImpl implements NewsApiService {

    private final NewsService newsService;

    public NewsApiServiceImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public Response addNews(CreateNews createNews, SecurityContext securityContext) {
        CreateOrUpdateNewsDto toCreate = CreateOrUpdateNewsDto.builder()
                .title(createNews.getTitle())
                .description(createNews.getDescription())
                .text(createNews.getText())
                .photoUrl(createNews.getPhotoUrl())
                .source(createNews.getSource())
                .dateTime(LocalDateTime.now())
                .build();
        News response = convertToNewsView(newsService.create(toCreate));
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response deleteNews(Long id, SecurityContext securityContext) {
        newsService.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response getAllNews(@Min(1)Integer pageNumber, @Min(0) @Max(10)Integer pageSize, SecurityContext securityContext) {
        List<ReadNews> readNewsList = newsService.getAll().stream()
                .map(newsDto -> convertToReadNews(newsDto))
                .collect(Collectors.toList());
        return Response.ok().entity(readNewsList).build();
    }

    @Override
    public Response getNews(Long id, SecurityContext securityContext) {
        ReadNewsDto dto = newsService.getById(id);
        ReadNews toResponse = convertToReadNews(dto);
        return Response.ok().entity(toResponse).build();
    }


    @Override
    public Response updateNews(Long id, UpdateNews updateNews, SecurityContext securityContext) {
        CreateOrUpdateNewsDto toUpdate = CreateOrUpdateNewsDto.builder()
                .title(updateNews.getTitle())
                .dateTime(LocalDateTime.now())
                .description(updateNews.getDescription())
                .text(updateNews.getText())
                .photoUrl(updateNews.getPhotoUrl())
                .source(updateNews.getSource())
                .build();

        News response = convertToNewsView(newsService.update(id, toUpdate));
        return Response.ok().entity(response).build();
    }

    private ReadNews convertToReadNews(ReadNewsDto dto) {
        ReadNews toResponse = new ReadNews();
        toResponse.setId(dto.getId());
        toResponse.setCreateOrUpdateDate(OffsetDateTime.of(dto.getCreatedAt(), ZoneOffset.UTC));
        toResponse.setTitle(dto.getTitle());
        toResponse.setDescription(dto.getDescription());
        toResponse.setText(dto.getText());
        toResponse.setPhotoUrl(dto.getPhotoUrl());
        toResponse.setSource(dto.getSource());
        return toResponse;
    }
    private News convertToNewsView(ReadNewsDto dto) {
        News toView = new News();
        toView.setId(dto.getId());
        toView.setCreateDate(OffsetDateTime.of(dto.getCreatedAt(), ZoneOffset.UTC));
        if(Objects.nonNull(dto.getUpdatedAt())) toView.setUpdateDate(OffsetDateTime.of(dto.getUpdatedAt(), ZoneOffset.UTC));
        toView.setTitle(dto.getTitle());
        toView.setDescription(dto.getDescription());
        toView.setText(dto.getText());
        toView.setPhotoUrl(dto.getPhotoUrl());
        toView.setSource(dto.getSource());
        return toView;
    }

}
