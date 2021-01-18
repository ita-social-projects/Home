package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.model.UpdateCooperation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateCooperationDtoMapper extends ViewToDtoMapper<UpdateCooperation, CooperationDto> {

    CooperationDto convertViewToDto(UpdateCooperation updateCooperation);
}
