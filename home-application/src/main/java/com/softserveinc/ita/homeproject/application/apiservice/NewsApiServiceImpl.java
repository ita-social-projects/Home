package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserApiServiceImpl class is the interlayer between generated
 * News controller and service layer of the application.
 *
 * @author Ihor Svyrydenko
 */
@Service
public class NewsApiServiceImpl implements NewsApiService {

    private final NewsService newsService;

    public NewsApiServiceImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * addNews method is implementation of HTTP POST
     * method, that is used to create a new news.
     *
     * @param createNews incoming data needed for creation of user
     * @return returns Response to generated controller
     */
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

    /**
     * deleteNews method is implementation of HTTP DELETE
     * method, that is used to delete news.
     *
     * @param id id of the news that needs to be deleted
     * @return returns Response to generated controller
     */
    @Override
    public Response deleteNews(Long id) {
        newsService.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * getAllNews method is implementation of HTTP GET
     * method, that is used to get all news from database.
     *
     * @param pageNumber number of the returned page with elements
     * @param pageSize amount of the returned elements
     * @return returns Response to generated controller
     */
    @Override
    public Response getAllNews(@Min(1)Integer pageNumber, @Min(0) @Max(10)Integer pageSize) {
        List<ReadNews> readNewsResponseList = newsService.getAll(pageNumber, pageSize).stream()
                .map(newsDto -> convertToReadNews(newsDto))
                .collect(Collectors.toList());
        return Response.ok().entity(readNewsResponseList).build();
    }

    /**
     * getNews method is implementation of HTTP GET method,
     * that is used to get one element of news by id from database.
     *
     * @param id id of the news in the database
     * @return returns Response to generated controller
     */
    @Override
    public Response getNews(Long id) {
        NewsDto readNewsDto = newsService.getById(id);
        ReadNews newsApiResponse = convertToReadNews(readNewsDto);
        return Response.ok().entity(newsApiResponse).build();
    }

    /**
     * updateNews method is implementation of HTTP PUT
     * method, that is update the existing news.
     *
     * @param id id of the news that needs to be updated
     * @param updateNews incoming data needed for news update
     * @return returns Response to generated controller
     */
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
