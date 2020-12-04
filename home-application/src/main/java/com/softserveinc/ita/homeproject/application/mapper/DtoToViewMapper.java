package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;

public interface DtoToViewMapper<ServiceDto extends BaseDto, ViewDto> {

    ViewDto convertDtoToView(ServiceDto serviceDto);
}
