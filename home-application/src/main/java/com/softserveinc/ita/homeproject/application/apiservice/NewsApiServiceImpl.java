package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.application.mapper.CreateNewsDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.ReadNewsDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.UpdateNewsDtoMapper;
import com.softserveinc.ita.homeproject.homeservice.service.NewsService;
import com.softserveinc.ita.homeproject.model.CreateNews;
import com.softserveinc.ita.homeproject.model.ReadNews;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsApiServiceImpl implements NewsApiService {

    private final NewsService newsService;
    private final CreateNewsDtoMapper createNewsDtoMapper;
    private final ReadNewsDtoMapper readNewsDtoMapper;
    private final UpdateNewsDtoMapper updateNewsDtoMapper;

    @Override
    public Response addNews(CreateNews createNews) {
        NewsDto newsDto = createNewsDtoMapper.convertViewToDto(createNews);
        NewsDto createdNewsDto = newsService.create(newsDto);
        ReadNews response = readNewsDtoMapper.convertDtoToView(createdNewsDto);

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
                .map(readNewsDtoMapper::convertDtoToView)
                .collect(Collectors.toList());

        return Response.ok().entity(readNewsResponseList).build();
    }

    @Override
    public Response getNews(Long id) {
        NewsDto readNewsDto = newsService.getById(id);
        ReadNews newsApiResponse = readNewsDtoMapper.convertDtoToView(readNewsDto);

        return Response.ok().entity(newsApiResponse).build();
    }

    @Override
    public Response updateNews(Long id, UpdateNews updateNews) {
        NewsDto updateNewsDto = updateNewsDtoMapper.convertViewToDto(updateNews);
        NewsDto readNewsDto = newsService.update(id, updateNewsDto);
        ReadNews response = readNewsDtoMapper.convertDtoToView(readNewsDto);
        return Response.ok().entity(response).build();
    }

}
