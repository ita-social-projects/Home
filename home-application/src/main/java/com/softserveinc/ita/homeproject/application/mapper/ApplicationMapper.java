package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.model.BaseView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {
    public BaseView convertToView(BaseDto dto, Class<? extends BaseView> view) {
        return new ModelMapper().map(dto, view);
    }

    public BaseDto convertToDto(BaseView view, Class<? extends BaseDto> dto) {
        return new ModelMapper().map(view, dto);
    }
}
