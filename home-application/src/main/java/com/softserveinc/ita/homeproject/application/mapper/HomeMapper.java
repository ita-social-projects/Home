package com.softserveinc.ita.homeproject.application.mapper;

import java.util.Set;
import javax.annotation.PostConstruct;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomeMapper {

    @SuppressWarnings("rawtypes")
    private final Set<HomeMappingConfig> homeMappingConfigs;

    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();
        for (HomeMappingConfig homeMappingConfig : homeMappingConfigs) {
            homeMappingConfig.addMappings(modelMapper);
        }
    }

    public <D> D convert(Object source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }
}
