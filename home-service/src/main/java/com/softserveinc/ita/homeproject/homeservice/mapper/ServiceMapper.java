package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {
    public BaseDto convertToDto(BaseEntity entity, Class<? extends BaseDto> dto) {
        return new ModelMapper().map(entity, dto);
    }

    public BaseEntity convertToEntity(BaseDto dto, Class<? extends BaseEntity> entity) {
        return new ModelMapper().map(dto, entity);
    }
}
