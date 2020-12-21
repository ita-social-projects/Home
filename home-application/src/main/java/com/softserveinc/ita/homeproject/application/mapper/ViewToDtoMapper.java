package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.model.BaseView;

public interface ViewToDtoMapper<ViewDto extends BaseView, ServiceDto extends BaseDto> {

    ServiceDto convertViewToDto(ViewDto viewDto);
}
