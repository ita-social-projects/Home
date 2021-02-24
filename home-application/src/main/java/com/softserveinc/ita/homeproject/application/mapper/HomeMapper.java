package com.softserveinc.ita.homeproject.application.mapper;

import java.util.Set;
import javax.annotation.PostConstruct;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomeMapper {

    @SuppressWarnings("rawtypes")
    private final Set<HomeMappingConfig> homeMappingConfigs;

    private ModelMapper modelMapper;

    @PostConstruct
    @SuppressWarnings({"unchecked cast", "rawtypes"})
    public void init() {
        modelMapper = new ModelMapper();
        for (HomeMappingConfig homeMappingConfig : homeMappingConfigs) {
            TypeMap typeMap =
                modelMapper.typeMap(homeMappingConfig.getSourceType(), homeMappingConfig.getDestinationType());
            homeMappingConfig.addMappings(typeMap);
        }
    }

    public <D> D convert(Object source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }
}
