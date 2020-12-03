package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserveinc.ita.homeproject.homeservice.constants.Permissions.*;

@Service
@RequiredArgsConstructor
public class NewsApiServiceImpl implements NewsApiService {

    private final NewsService newsService;

    @PreAuthorize(CREATE_NEWS_PERMISSION)
    @Override
    public Response addNews(CreateNews createNews) {
        NewsDto newsDto = NewsDto.builder()
                .title(createNews.getTitle())
                .description(createNews.getDescription())
                .text(createNews.getText())
                .photoUrl(createNews.getPhotoUrl())
                .source(createNews.getSource())
                .build();
        ReadNews response = convertToReadNews(newsService.create(newsDto));
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PreAuthorize(DELETE_NEWS_PERMISSION)
    @Override
    public Response deleteNews(Long id) {
        newsService.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(GET_NEWS_PERMISSION)
    @Override
    public Response getAllNews(@Min(1)Integer pageNumber, @Min(0) @Max(10)Integer pageSize) {
        List<ReadNews> readNewsResponseList = newsService.getAll(pageNumber, pageSize).stream()
                .map(newsDto -> convertToReadNews(newsDto))
                .collect(Collectors.toList());
        return Response.ok().entity(readNewsResponseList).build();
    }

    @PreAuthorize(GET_NEWS_PERMISSION)
    @Override
    public Response getNews(Long id) {
        NewsDto readNewsDto = newsService.getById(id);
        ReadNews newsApiResponse = convertToReadNews(readNewsDto);
        return Response.ok().entity(newsApiResponse).build();
    }

    @PreAuthorize(UPDATE_NEWS_PERMISSION)
    @Override
    public Response updateNews(Long id, UpdateNews updateNews) {
        NewsDto updateNewsDto = NewsDto.builder()
                .title(updateNews.getTitle())
                .description(updateNews.getDescription())
                .text(updateNews.getText())
                .photoUrl(updateNews.getPhotoUrl())
                .source(updateNews.getSource())
                .build();

        ReadNews response = convertToReadNews(newsService.update(id, updateNewsDto));
        return Response.ok().entity(response).build();
    }

    private ReadNews convertToReadNews(NewsDto readNewsDto) {
        ReadNews toResponse = new ReadNews();
        toResponse.setId(readNewsDto.getId());
        toResponse.setTitle(readNewsDto.getTitle());
        toResponse.setDescription(readNewsDto.getDescription());
        toResponse.setText(readNewsDto.getText());
        toResponse.setPhotoUrl(readNewsDto.getPhotoUrl());
        toResponse.setSource(readNewsDto.getSource());
        return toResponse;
    }

}
