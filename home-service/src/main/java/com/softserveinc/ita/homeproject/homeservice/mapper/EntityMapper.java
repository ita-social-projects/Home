package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;

public interface EntityMapper {

    <Dto extends BaseDto> Dto convertToDto(BaseEntity entity, Dto dto);

    <Entity extends BaseEntity> Entity convertToEntity(BaseDto dto, Entity entity);
}
