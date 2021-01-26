package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;

public interface IServiceMapper {

    <Dto extends BaseDto> Dto convertToDto(BaseEntity entity, Class<Dto> dto);

    <Entity extends BaseEntity> Entity convertToEntity(BaseDto dto, Class<Entity> entity);
}
