package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.application.mapper.CreateNewsDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.ReadNewsDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.UpdateNewsDtoMapper;
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

import static com.softserveinc.ita.homeproject.application.constants.Permissions.*;

/**
 * NewsApiServiceImpl class is the inter layer between generated
 * News controller and service layer of the application
 *
 * @author Ihor Svyrydenko
 */
@Service
@RequiredArgsConstructor
public class NewsApiServiceImpl implements NewsApiService {

    private final NewsService newsService;
    private final CreateNewsDtoMapper createNewsDtoMapper;
    private final ReadNewsDtoMapper readNewsDtoMapper;
    private final UpdateNewsDtoMapper updateNewsDtoMapper;

    /**
     * addNews method is implementation of HTTP POST
     * method to create a new news
     *
     * @param createNews are incoming data needed for creation new news
     * @return returns Response to generated controller
     */
    @PreAuthorize(CREATE_NEWS_PERMISSION)
    @Override
    public Response addNews(CreateNews createNews) {
        NewsDto newsDto = createNewsDtoMapper.convertViewToDto(createNews);
        NewsDto createdNewsDto = newsService.create(newsDto);
        ReadNews response = readNewsDtoMapper.convertDtoToView(createdNewsDto);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    /**
     * deleteNews method is implementation of HTTP DELETE
     * method to delete news
     *
     * @param id is the id of the news that has to be deleted
     * @return returns Response to generated controller
     */
    @PreAuthorize(DELETE_NEWS_PERMISSION)
    @Override
    public Response deleteNews(Long id) {
        newsService.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * getAllNews method is implementation of HTTP GET
     * method to get all news from database
     *
     * @param pageNumber is the number of the returned page with elements
     * @param pageSize is amount of the returned elements
     * @return returns Response to generated controller
     */
    @PreAuthorize(GET_NEWS_PERMISSION)
    @Override
    public Response getAllNews(@Min(1)Integer pageNumber, @Min(0) @Max(10)Integer pageSize) {
        List<ReadNews> readNewsResponseList = newsService.getAll(pageNumber, pageSize).stream()
                .map(readNewsDtoMapper::convertDtoToView)
                .collect(Collectors.toList());

        return Response.ok().entity(readNewsResponseList).build();
    }

    /**
     * getNews method is implementation of HTTP GET method
     * to get news by id from database
     *
     * @param id is news' id in the database
     * @return returns Response to generated controller
     */
    @PreAuthorize(GET_NEWS_PERMISSION)
    @Override
    public Response getNews(Long id) {
        NewsDto readNewsDto = newsService.getById(id);
        ReadNews newsApiResponse = readNewsDtoMapper.convertDtoToView(readNewsDto);

        return Response.ok().entity(newsApiResponse).build();
    }

    /**
     * updateNews method is implementation of HTTP PUT
     * method to update the existing news
     *
     * @param id is id of the news that has to be updated
     * @param updateNews are incoming data needed for news update
     * @return returns Response to generated controller
     */
    @PreAuthorize(UPDATE_NEWS_PERMISSION)
    @Override
    public Response updateNews(Long id, UpdateNews updateNews) {
        NewsDto updateNewsDto = updateNewsDtoMapper.convertViewToDto(updateNews);
        NewsDto readNewsDto = newsService.update(id, updateNewsDto);
        ReadNews response = readNewsDtoMapper.convertDtoToView(readNewsDto);
        return Response.ok().entity(response).build();
    }

}
