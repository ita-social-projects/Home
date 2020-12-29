package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.model.BaseView;

public interface DtoToViewMapper<ServiceDto extends BaseDto, ViewDto extends BaseView> {

    ViewDto convertDtoToView(ServiceDto serviceDto);
}
