package com.softserveinc.ita.homeproject.homeservice.mapperViewToDto;

import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.model.CreateNews;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateNewsDtoMapper extends ViewToDtoMapper<CreateNews, NewsDto>{

    NewsDto convertViewToDto(CreateNews createNews);
}
