package com.softserveinc.ita.homeproject.apiservice;

import com.softserveinc.ita.homeproject.api.ApiResponseMessage;
import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.api.NotFoundException;
import com.softserveinc.ita.homeproject.model.PostNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Service
public class NewsApiServiceImpl implements NewsApiService {

    @Override
    @PreAuthorize("hasAuthority('CREATE_NEWS_PERMISSION')")
    public Response addNews(PostNews postNews, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    @PreAuthorize("hasAuthority('DELETE_NEWS_PERMISSION')")
    public Response deleteNews(Integer id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    @PreAuthorize("hasAuthority('READ_NEWS_PERMISSION')")
    public Response getAllNews(Integer pageNumber, Integer pageSize, String type, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    @PreAuthorize("hasAuthority('READ_NEWS_PERMISSION')")
    public Response getNews(Integer id, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    @PreAuthorize("hasAuthority('UPDATE_NEWS_PERMISSION')")
    public Response updateNews(Integer id, UpdateNews updateNews, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
