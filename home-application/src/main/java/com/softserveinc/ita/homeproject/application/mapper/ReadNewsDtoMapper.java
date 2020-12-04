package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import com.softserveinc.ita.homeproject.model.ReadNews;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadNewsDtoMapper extends DtoToViewMapper<NewsDto, ReadNews>{

    ReadNews convertDtoToView(NewsDto newsDto);
}
