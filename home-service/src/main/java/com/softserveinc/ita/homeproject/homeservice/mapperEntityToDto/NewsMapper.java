package com.softserveinc.ita.homeproject.homeservice.mapperEntityToDto;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import com.softserveinc.ita.homeproject.homeservice.dto.NewsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsMapper extends EntityToDtoMapper<NewsDto, News> {

    News convertDtoToEntity(NewsDto newsDto);

    NewsDto convertEntityToDto(News news);
}
