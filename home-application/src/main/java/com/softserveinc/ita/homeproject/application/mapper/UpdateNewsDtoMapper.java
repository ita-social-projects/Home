package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.model.UpdateNews;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateNewsDtoMapper extends ViewToDtoMapper<UpdateNews, NewsDto>{

    NewsDto convertViewToDto(UpdateNews updateNews);
}
