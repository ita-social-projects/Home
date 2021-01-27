package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.CustomServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

    private ModelMapper modelMapper;

    private final Set<CustomServiceMappingConfig> customMappingConfigs;

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();
        for (CustomServiceMappingConfig customServiceMappingConfig : customMappingConfigs) {
            customServiceMappingConfig.addMapping(modelMapper);
        }
    }


    public <Dto extends BaseDto> Dto convertToDto(BaseEntity entity, Class<Dto> dto) {
        return modelMapper.map(entity, (Type) dto);
    }

    public <Entity extends BaseEntity> Entity convertToEntity(BaseDto dto, Class<Entity> entity) {
        return modelMapper.map(dto, (Type) entity);
    }
}
