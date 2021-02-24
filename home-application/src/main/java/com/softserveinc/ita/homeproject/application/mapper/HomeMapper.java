package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HomeMapper {

    private ModelMapper modelMapper;

    @SuppressWarnings("rawtypes")
    private final Set<HomeMappingConfig> homeMappingConfigs;


    @PostConstruct
    @SuppressWarnings({"unchecked cast", "rawtypes"})
    public void init() {
        modelMapper = new ModelMapper();
        for (HomeMappingConfig homeMappingConfig : homeMappingConfigs) {
            TypeMap typeMap = modelMapper.typeMap(homeMappingConfig.getSourceType(), homeMappingConfig.getDestinationType());
            homeMappingConfig.addMappings(typeMap);
        }
    }

    public <D> D convert(Object source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }
}
