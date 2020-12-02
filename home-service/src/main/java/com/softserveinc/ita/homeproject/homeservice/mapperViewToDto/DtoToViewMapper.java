package com.softserveinc.ita.homeproject.homeservice.mapperViewToDto;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;

public interface DtoToViewMapper<ServiceDto extends BaseDto, ViewDto> {

    ViewDto convertDtoToView(ServiceDto serviceDto);
}
