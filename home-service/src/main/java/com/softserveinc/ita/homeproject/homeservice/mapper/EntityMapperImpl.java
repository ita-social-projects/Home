package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@RequiredArgsConstructor
public class EntityMapperImpl implements EntityMapper {

    private final ModelMapper mapper;

    @Override
    public <Dto extends BaseDto> Dto convertToDto(BaseEntity entity, Class<Dto> dto) {
        return mapper.map(entity, (Type) dto);
    }

    @Override
    public <Entity extends BaseEntity> Entity convertToEntity(BaseDto dto, Class<Entity> entity) {
        return mapper.map(dto, (Type) entity);
    }
}
