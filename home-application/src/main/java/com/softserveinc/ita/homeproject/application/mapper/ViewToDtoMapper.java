package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;

public interface ViewToDtoMapper<ViewDto, ServiceDto extends BaseDto> {

    ServiceDto convertViewToDto(ViewDto viewDto);
}
