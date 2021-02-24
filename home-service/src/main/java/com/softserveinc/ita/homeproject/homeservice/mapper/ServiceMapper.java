package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ServiceMapper {

    private ModelMapper modelMapper;

    @SuppressWarnings("rawtypes")
    private final Set<ServiceMappingConfig> homeMappingConfigs;


    @PostConstruct
    @SuppressWarnings({"unchecked cast", "rawtypes"})
    public void init() {
        modelMapper = new ModelMapper();
        for (ServiceMappingConfig homeMappingConfig : homeMappingConfigs) {
            TypeMap typeMap = modelMapper.typeMap(homeMappingConfig.getSourceType(), homeMappingConfig.getDestinationType());
            homeMappingConfig.addMappings(typeMap);
        }
    }

    public <D> D convert(Object source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }
}
