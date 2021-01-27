package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.application.mapper.config.CustomHomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.model.BaseView;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HomeMapper {

    private ModelMapper modelMapper;
    private final Set<CustomHomeMappingConfig> customHomeMappingConfigs;


    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();
        for (CustomHomeMappingConfig customHomeMappingConfig : customHomeMappingConfigs) {
            customHomeMappingConfig.addMapping(modelMapper);
        }
    }


    public <View extends BaseView> View convertToView(BaseDto dto, Class<View> view) {
        return modelMapper.map(dto, (Type) view);
    }

    public <Dto extends BaseDto> Dto convertToDto(BaseView view, Class<Dto> dto) {
        return modelMapper.map(view, (Type) dto);
    }
}
