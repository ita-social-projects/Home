package com.softserveinc.ita.homeproject.homeservice.mapper;

import java.util.Set;
import javax.annotation.PostConstruct;

import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

    @SuppressWarnings("rawtypes")
    private final Set<ServiceMappingConfig> homeMappingConfigs;

    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();
        for (ServiceMappingConfig homeMappingConfig : homeMappingConfigs) {
            homeMappingConfig.addMappings(modelMapper);
        }
    }

    public <D> D convert(Object source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }
}
