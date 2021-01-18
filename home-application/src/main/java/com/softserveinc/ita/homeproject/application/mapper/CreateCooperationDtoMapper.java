package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateCooperationDtoMapper extends ViewToDtoMapper<CreateCooperation, CooperationDto>{

    CooperationDto convertViewToDto(CreateCooperation createCooperation);
}
