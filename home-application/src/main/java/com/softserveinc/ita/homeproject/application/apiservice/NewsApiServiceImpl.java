package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.ApiResponseMessage;
import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.api.NotFoundException;
import com.softserveinc.ita.homeproject.model.PostNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import com.softserveinc.ita.homeproject.service.NewsService;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class NewsApiServiceImpl implements NewsApiService {

    private final NewsService newsService;

    public NewsApiServiceImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public Response addNews(PostNews postNews, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response deleteNews(Integer id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getAllNews(Integer pageNumber, Integer pageSize, String type, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getNews(Integer id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateNews(Integer id, UpdateNews updateNews, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
