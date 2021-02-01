package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homeservice.query.impl.NewsQueryConfig;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.*;

/**
 * NewsApiServiceImpl class is the inter layer between generated
 * News controller and service layer of the application.
 *
 * @author Ihor Svyrydenko
 */

@Provider
@NoArgsConstructor
public class NewsApiImpl extends CommonApiService<NewsDto> implements NewsApi {

    private NewsService newsService;
    private HomeMapper mapper;
    private EntitySpecificationService entitySpecificationService;


    /**
     * addNews method is implementation of HTTP POST
     * method for creating a new news.
     *
     * @param createNews are incoming data needed for creation new news
     * @return Response to generated controller
     */
    @PreAuthorize(CREATE_NEWS_PERMISSION)
    @Override
    public Response addNews(CreateNews createNews) {
        NewsDto newsDto = mapper.convert(createNews, NewsDto.class);
        NewsDto createdNewsDto = newsService.create(newsDto);
        ReadNews response = mapper.convert(createdNewsDto, ReadNews.class);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    /**
     * deleteNews method is implementation of HTTP DELETE
     * method for deleting news.
     *
     * @param id is the id of the news that has to be deleted
     * @return Response to generated controller
     */
    @PreAuthorize(DELETE_NEWS_PERMISSION)
    @Override
    public Response deleteNews(Long id) {
        newsService.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * getAllNews method is implementation of HTTP GET
     * method for getting all news from database.
     *
     * @param pageNumber is the number of the returned page with elements
     * @param pageSize   is amount of the returned elements
     * @return Response to generated controller
     */
    @PreAuthorize(GET_NEWS_PERMISSION)
    @Override
    public Response getAllNews(@Min(1) Integer pageNumber,
                               @Min(0) @Max(10) Integer pageSize,
                               String sort,
                               String search,
                               String id,
                               String title,
                               String text,
                               String source) {

        Map<QueryParamEnum, String> filterMap = new HashMap<>();

        filterMap.put(NewsQueryConfig.NewsQueryParamEnum.ID, id);
        filterMap.put(NewsQueryConfig.NewsQueryParamEnum.TITLE, title);
        filterMap.put(NewsQueryConfig.NewsQueryParamEnum.TEXT, text);
        filterMap.put(NewsQueryConfig.NewsQueryParamEnum.SOURCE, source);

        Page<NewsDto> readNews = newsService.findNews(
                pageNumber,
                pageSize,
                entitySpecificationService.getSpecification(filterMap, search, sort)
        );

        return buildQueryResponse(readNews, ReadNews.class);
    }

    /**
     * getNews method is implementation of HTTP GET method
     * for getting news by id from database.
     *
     * @param id is id of the news in the database
     * @return Response to generated controller
     */
    @PreAuthorize(GET_NEWS_PERMISSION)
    @Override
    public Response getNews(Long id) {
        NewsDto readNewsDto = newsService.getById(id);
        ReadNews newsApiResponse = mapper.convert(readNewsDto, ReadNews.class);

        return Response.ok().entity(newsApiResponse).build();
    }

    /**
     * updateNews method is implementation of HTTP PUT
     * method for updating existing news.
     *
     * @param id         is id of the news that has to be updated
     * @param updateNews are incoming data needed for news update
     * @return Response to generated controller
     */
    @PreAuthorize(UPDATE_NEWS_PERMISSION)
    @Override
    public Response updateNews(Long id, UpdateNews updateNews) {
        NewsDto updateNewsDto = mapper.convert(updateNews, NewsDto.class);
        NewsDto readNewsDto = newsService.update(id, updateNewsDto);
        ReadNews response = mapper.convert(readNewsDto, ReadNews.class);
        return Response.ok().entity(response).build();
    }


    @Autowired
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    @Autowired
    public void setMapper(HomeMapper mapper) {
        super.setMapper(mapper);
        this.mapper = mapper;
    }

    @Autowired
    public void setSpecificationService(EntitySpecificationService entitySpecificationService) {
        this.entitySpecificationService = entitySpecificationService;
    }
}
