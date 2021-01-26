package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.application.mapper.config.CustomHomeMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HomeMapper extends ModelMapper {

    private final Set<CustomHomeMappingConfig> customHomeMappingConfigs;

    @PostConstruct
    public void init() {
        for (CustomHomeMappingConfig customHomeMappingConfig : customHomeMappingConfigs) {
            customHomeMappingConfig.addMapping(this);
        }
    }
}
