package com.softserveinc.ita.homeproject.homeservice.mapper;

import javax.annotation.PostConstruct;

import java.util.Set;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.AbstractTypeConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.ConditionalConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

    private static final String ENTITY_PACKAGE = "com.softserveinc.ita.homeproject.homedata.entity";
    private static final String DTO_PACKAGE = "com.softserveinc.ita.homeproject.homeservice.dto";

    @SuppressWarnings("rawtypes")
    private final Set<ServiceMappingConfig> homeMappingConfigs;

    private ModelMapper modelMapper;

    @PostConstruct
    @SuppressWarnings({"unchecked cast", "rawtypes"})
    public void init() {
        modelMapper = new ModelMapper();

        ConditionalConverter<BaseEntity, BaseDto> conditionalConverter = new AbstractTypeConverter<>(ENTITY_PACKAGE, DTO_PACKAGE);
        ConditionalConverter<BaseDto, BaseEntity> conditionalConverter2 = new AbstractTypeConverter<>(DTO_PACKAGE, ENTITY_PACKAGE);

        modelMapper.getConfiguration().getConverters().add(0, conditionalConverter);
        modelMapper.getConfiguration().getConverters().add(0, conditionalConverter2);

        for (ServiceMappingConfig homeMappingConfig : homeMappingConfigs) {
            TypeMap typeMap =
                modelMapper.typeMap(homeMappingConfig.getSourceType(), homeMappingConfig.getDestinationType());
            homeMappingConfig.addMappings(typeMap);
        }
    }

    public <D> D convert(Object source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }
}
