package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homeservice.config.CustomServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ServiceMapper extends ModelMapper {

    private final Set<CustomServiceMappingConfig> customMappingConfigs;

    @PostConstruct
    public void init() {
        for (CustomServiceMappingConfig customServiceMappingConfig : customMappingConfigs) {
            customServiceMappingConfig.addMapping(this);
        }
    }
}
