package com.softserveinc.ita.homeproject.homeservice.mapperentity;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;

public interface EntityToDtoMapper<Dto extends BaseDto, Entity extends BaseEntity> {

    Entity convertDtoToEntity(Dto dto);
    Dto convertEntityToDto(Entity entity);
}
